package io.github.sahalnazar.stickyfooter

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import io.github.sahalnazar.stickyfooter.databinding.ActivityMainBinding
import io.github.sahalnazar.stickyfooter.utils.ExtensionFunctions.getViewBinding

class MainActivity : AppCompatActivity(), View.OnTouchListener {
    private lateinit var binding: ActivityMainBinding

    private var counter = 0
    private var isCollapsed = true

    private val viewModel: LottieButtonViewModel by viewModels()
    private val lottieCheckButton: LottieAnimationView
        get() = findViewById(R.id.animation_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding(R.layout.activity_main)

        setContentView(R.layout.activity_lottie)

        //Lottie
//        setObservers()
        setClickListeners()
//
//        setupStickyFooter()
//        setupClickListeners()
    }

    private fun setObservers() {
        viewModel.isCheckButtonActive.observe(this, ::onFavButtonStateUpdated)
    }

    private fun onFavButtonStateUpdated(isActive: Boolean) {
        lottieCheckButton.progress = if (isActive) 1f else 0f
    }

    private fun setClickListeners() {
        lottieCheckButton.setOnClickListener {
            if (viewModel.isCheckButtonActive.value == false) {
                lottieCheckButton.setMinAndMaxFrame(0, 100)
                lottieCheckButton.playAnimation()
                lottieCheckButton.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {
                        // Ignore
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        lottieCheckButton.removeAnimatorListener(this)
                        viewModel.toggleCheckButtonState()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        // Ignore
                    }

                    override fun onAnimationRepeat(animation: Animator?) {
                        // Ignore
                    }
                })
            } else {
                lottieCheckButton.setMinAndMaxFrame(101, 160)
                lottieCheckButton.playAnimation()
                lottieCheckButton.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {
                        // Ignore
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        lottieCheckButton.removeAnimatorListener(this)
                        viewModel.toggleCheckButtonState()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        // Ignore
                    }

                    override fun onAnimationRepeat(animation: Animator?) {
                        // Ignore
                    }
                })
            }
        }
    }














    private fun setupClickListeners() {
//        binding.btnBuyNowSticky.setOnClickListener {
//            counter += 1
//            Toast.makeText(this, "clicked on buy now #Sticky", Toast.LENGTH_SHORT).show()
//        }
//        binding.btnBuyNowFixed.setOnClickListener {
//            counter += 1
//            Toast.makeText(this, "clicked on buy now #Fixed", Toast.LENGTH_SHORT).show()
//        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupStickyFooter() {

        binding.nestedScrollView.setOnTouchListener(this)
        binding.nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            val view = binding.nestedScrollView.getChildAt(binding.nestedScrollView.childCount - 1)
            val topDetector = binding.nestedScrollView.scrollY
            val bottomDetector: Int =
                view.bottom - (binding.nestedScrollView.height + binding.nestedScrollView.scrollY)
            if (bottomDetector == 0) {
                slideView(
                    binding.clBuyNowSticky,
                    binding.clBuyNowSticky.layoutParams.height,
                    336,
                    false
                )
            }

            if (topDetector <= 0)
                slideView(
                    binding.clBuyNowSticky,
                    binding.clBuyNowSticky.layoutParams.height,
                    168,
                    true
                )

        }
    }

//        if(binding.nestedScrollView.isViewVisible())
//            Toast.makeText(baseContext, "Scroll View bottom reached", Toast.LENGTH_SHORT).show()

//        binding.clBuyNowSticky.isVisible = binding.nestedScrollView.isViewVisible() == false
//        binding.nestedScrollView.setOnScrollChangeListener { _, _, _, _, _ ->
//            binding.clBuyNowSticky.isVisible = binding.nestedScrollView.isViewVisible(binding.clBuyNowFixed) == false
//        }


    fun slideView(view: View, currentHeight: Int, newHeight: Int, collapse: Boolean) {
        animateSize(view, currentHeight, newHeight)
        animateColor(view, collapse)
    }

    private fun animateSize(view: View, currentHeight: Int, newHeight: Int) {
        val slideAnimator = ValueAnimator
            .ofInt(currentHeight, newHeight)
            .setDuration(250)

        slideAnimator.addUpdateListener { animation1 ->
            val value = animation1.animatedValue as Int
            view.layoutParams.height = value
            view.requestLayout()
        }

        val animationSet = AnimatorSet()
        animationSet.interpolator = AccelerateDecelerateInterpolator()
        animationSet.play(slideAnimator)
        animationSet.start()
    }

    private fun animateColor(view: View, collapse: Boolean) {

//        view.setBackgroundColor(if (collapse) resources.getColor(R.color.btnCollapsed, null) else resources.getColor(R.color.black, null))
        val colorAnimator = if (collapse) collapseColorsAnimator() else expandColorsAnimator()
        colorAnimator.duration = 250
        colorAnimator.addUpdateListener {
            view.setBackgroundColor(it.animatedValue as Int)
        }

        colorAnimator.start()

        updateTextValueAndColor(collapse)
    }

    private fun collapseColorsAnimator() = ValueAnimator.ofObject(
        ArgbEvaluator(),
        resources.getColor(R.color.black, null),
        resources.getColor(R.color.btnCollapsed, null)
    )

    private fun expandColorsAnimator() = ValueAnimator.ofObject(
        ArgbEvaluator(),
        resources.getColor(R.color.btnCollapsed, null),
        resources.getColor(R.color.black, null)
    )

    private fun collapseTextColorsAnimator() = ValueAnimator.ofObject(
        ArgbEvaluator(),
        resources.getColor(R.color.white, null),
        resources.getColor(R.color.black, null)
    )

    private fun expandTextColorsAnimator() = ValueAnimator.ofObject(
        ArgbEvaluator(),
        resources.getColor(R.color.black, null),
        resources.getColor(R.color.white, null)
    )

    private fun updateTextValueAndColor(collapse: Boolean) {
        val colorAnimator = if (collapse) collapseTextColorsAnimator() else expandTextColorsAnimator()
        colorAnimator.duration = 250
        colorAnimator.addUpdateListener {
            binding.tvButton.setTextColor(it.animatedValue as Int)
            binding.tvButton.text = if (collapse) "Confirmar Pix com emprestimo" else "Confirmar e Finalizar"
        }

        colorAnimator.start()
    }
}