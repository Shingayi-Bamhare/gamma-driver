(ns gamma.webgl.draw
  (:require
    [gamma.webgl.api :as p]))


(defrecord DrawArrays [mode first count]
  p/IOperator
  (operate! [this target]
    (let [gl (p/gl target)]
      (.drawArrays gl mode first count))))

(defn draw-arrays [mode first count]
  (DrawArrays. mode first count))


(defn draw-elements [mode count type offset]
  (reify p/IOperator
    (operate! [this target]
      (let [gl (p/gl target)]
        (.drawElements gl mode count type offset)))))

(comment

  (defn clear [gl spec])

  (defn mask [gl spec])


  (def draw-modes
    {:lines ggl/LINES
     :line-strip ggl/LINE_STRIP
     :line-loop ggl/LINE_LOOP
     :points ggl/POINTS
     :triangle-strip ggl/TRIANGLE_STRIP
     :triangle-fan ggl/TRIANGLE_FAN
     :triangles ggl/TRIANGLES})

  (defn draw-arrays
    ([gl program opts]
     (.useProgram gl (:program program))
     (.drawArrays
       gl
       (draw-modes (:draw-mode opts :triangles))
       (:first opts 0)
       (:count opts)))
    ([gl program opts target]
     (if target
       (do
         (.bindFramebuffer gl ggl/FRAMEBUFFER (:frame-buffer target))
         (draw-arrays gl program opts)
         (.bindFramebuffer gl ggl/FRAMEBUFFER nil)
         target)
       (draw-arrays gl program opts))))


  (def element-types
    {:unsigned-byte ggl/UNSIGNED_BYTE
     :unsigned-short ggl/UNSIGNED_SHORT})

  (defn draw-elements
    ([gl program opts]
     (let [draw-mode (let [dm (:draw-mode opts)]
                       (if (integer? dm)
                         dm
                         (draw-modes (:draw-mode opts))))
           cnt       (:count opts)
           data-type (element-types (:index-type opts) ggl/UNSIGNED_SHORT)
           offset    (* (get {:unsigned-byte 1 :unsigned-short 2} (:index-type opts)) (:first opts))]
       (.useProgram gl (:program program))
       (.drawElements gl draw-mode cnt data-type offset)))
    ([gl program opts target]
     (if target
       (do
         (.bindFramebuffer gl ggl/FRAMEBUFFER (:frame-buffer target))
         (draw-elements gl program opts)
         (.bindFramebuffer gl ggl/FRAMEBUFFER nil)
         target)
       (draw-elements gl program opts))))

  )