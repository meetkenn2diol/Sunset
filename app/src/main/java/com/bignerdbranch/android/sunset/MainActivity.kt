package com.bignerdbranch.android.sunset

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var sceneView: View
    private lateinit var sunView: View
    private lateinit var skyView: View
    private var shouldReverse: Boolean = false

    /**
     * Retrieve the SkyBlueColor from the color resources
     */
    private val blueSkyColor: Int by lazy { ContextCompat.getColor(this, R.color.blue_sky) }
    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sceneView = findViewById(R.id.scene)
        sunView = findViewById(R.id.sun)
        skyView = findViewById(R.id.sky)

        //Starting animation on press
        sceneView.setOnClickListener {
            startOrReverseAnimation()
        }
    }

    private fun startOrReverseAnimation() {
        when {
            shouldReverse -> reverseAnimation()
            else -> startAnimation()
        }
    }

    /**
     * In any event, the animation will start with the top of the view at its current location. It needs to end with the top at the bottom of sunView’s parent, skyView.
     */
    private fun startAnimation() {
        //get where the animator should start and where it should end
        val sunYStart = sunView.top.toFloat()
        val sunYEnd = skyView.height.toFloat()

        //Creating a sun animator for height
        val heightAnimator = ObjectAnimator
            .ofFloat(sunView, "y", sunYStart, sunYEnd)
            .setDuration(3000)
        // Adding acceleration
        heightAnimator.interpolator = AccelerateInterpolator()
        //an animation to startAnimation() to animate the sky from blueSkyColor to
        //sunsetSkyColor.
        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", sunsetSkyColor, nightSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        //using AnimationSet to order the sequence of your animation
        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
            .with(sunsetSkyAnimator)
            .before(nightSkyAnimator)
        animatorSet.start()

        shouldReverse = true
    }

    /**
     *Member function to reverse the animation of startAnimation()
     */
    private fun reverseAnimation() {
        //get where the animator should start and where it should end
        val sunYStart = skyView.height.toFloat()
        val sunYEnd = sunView.top.toFloat()


        //Creating a sun animator for height
        val heightAnimator = ObjectAnimator
            .ofFloat(sunView, "y", sunYStart, sunYEnd)
            .setDuration(3000)
        // Adding acceleration
        heightAnimator.interpolator = AccelerateInterpolator()
        //an animation to startAnimation() to animate the sky from blueSkyColor to
        //sunsetSkyColor.
        val sunriseSkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", sunsetSkyColor, blueSkyColor)
            .setDuration(3000)
        sunriseSkyAnimator.setEvaluator(ArgbEvaluator())
//animate to a night sky
        val daySkyAnimator = ObjectAnimator
            .ofInt(skyView, "backgroundColor", nightSkyColor, sunsetSkyColor)
            .setDuration(1500)
        daySkyAnimator.setEvaluator(ArgbEvaluator())

        //using AnimationSet to order the sequence of your animation
        val animatorSet = AnimatorSet()
        animatorSet.play(daySkyAnimator)
            .with(heightAnimator)
            .before(sunriseSkyAnimator)
        animatorSet.start()

        shouldReverse = false
    }
}