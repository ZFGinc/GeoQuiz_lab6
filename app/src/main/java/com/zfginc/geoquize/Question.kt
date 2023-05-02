package com.zfginc.geoquize

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer : Boolean) {
    var isAnswered: Boolean? = null;
    var isCheating: Boolean? = false;
}