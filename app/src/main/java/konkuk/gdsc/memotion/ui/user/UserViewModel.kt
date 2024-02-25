package konkuk.gdsc.memotion.ui.user

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import konkuk.gdsc.memotion.domain.entity.user.User
import konkuk.gdsc.memotion.util.TAG
import konkuk.gdsc.memotion.util.VIEWMODEL

class UserViewModel : ViewModel() {
    private val _profile = MutableLiveData<User>()
    val profile: LiveData<User> = _profile

    private val _notificationTime = MutableLiveData<Calendar>()
    val notificationTime: LiveData<Calendar> = _notificationTime

    init {
        _notificationTime.value = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 22)
            set(Calendar.MINUTE, 0)
            set(Calendar.MILLISECOND, 0)
        }
        _profile.value = User(
            name = "You need to login to use\n“My Page” service.",
            imageUrl = "",
        )
    }

    fun setProfile(info: User) {
        _profile.value = info
    }

    fun updateNotificationTime(hour: Int, minute: Int) {
        Log.d(TAG, "UserViewModel - updateNotificationTime() called\n안")
        _notificationTime.value = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.MILLISECOND, 0)
        }
    }


}