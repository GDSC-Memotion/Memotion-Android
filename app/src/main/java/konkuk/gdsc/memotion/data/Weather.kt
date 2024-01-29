package konkuk.gdsc.memotion.data

import konkuk.gdsc.memotion.R

enum class Weather(val number: Int) {
    SUN(0),
    SEMI_CLOUDY(1),
    CLOUDY(2),
    RAINY(3),
    SNOW(4);

    fun getResource(): Int {
        return when(this) {
            SUN -> R.drawable.icon_sun_un
            SEMI_CLOUDY -> R.drawable.icon_semi_cloudy_un
            CLOUDY -> R.drawable.icon_cloudy_un
            RAINY -> R.drawable.icon_rainy_un
            SNOW -> R.drawable.icon_snow_un
        }
    }
}