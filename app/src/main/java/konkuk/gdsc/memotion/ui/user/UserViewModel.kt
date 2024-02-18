package konkuk.gdsc.memotion.ui.user

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import konkuk.gdsc.memotion.domain.entity.user.User

class UserViewModel : ViewModel() {
    private val _profile = MutableLiveData<User>()
    val profile: LiveData<User> = _profile

    private val _notificationTime = MutableLiveData<Calendar>()
    val notificationTime: LiveData<Calendar> = _notificationTime
}