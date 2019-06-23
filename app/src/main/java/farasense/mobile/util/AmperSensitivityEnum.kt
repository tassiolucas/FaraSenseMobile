package farasense.mobile.util

enum class AmperSensitivityEnum(val value: Float) {
    ZERO(10F),
    ONE(15F),
    TWO(20F),
    THREE(25F),
    FOUR(30F);

    companion object {
        fun getValueFromSelection(selection: Int = 0) : Float {
            return when(selection) {
                4 -> FOUR.value
                3 -> THREE.value
                2 -> TWO.value
                1 -> ONE.value
                else -> ZERO.value
            }
        }

        fun getSelectionFromValue(value: Float) : Int {
            return when(value) {
                30F -> FOUR.ordinal
                25F -> THREE.ordinal
                20F -> TWO.ordinal
                15F -> ONE.ordinal
                else -> ZERO.ordinal
            }
        }
    }
}