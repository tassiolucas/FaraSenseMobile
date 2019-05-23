package farasense.mobile.util

enum class SensitySelectorEnum(val value: Float, val description: String) {
    ONE(0.01F, "10 Amper"),
    TWO(0.20F, "8 Amper"),
    THREE(0.40F, "4 Amper"),
    FOUR(0.80F, "2 Amper"),
    FIVE(1.00F, "1 Amper")
}