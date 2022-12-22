package io.github.sahalnazar.stickyfooter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LottieButtonViewModel : ViewModel() {

    private val _isCheckButtonActive = MutableLiveData(false)
    val isCheckButtonActive: LiveData<Boolean> = _isCheckButtonActive

    fun toggleCheckButtonState() {
        _isCheckButtonActive.value = !(_isCheckButtonActive.value ?: false)
    }
}