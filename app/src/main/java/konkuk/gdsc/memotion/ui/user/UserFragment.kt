package konkuk.gdsc.memotion.ui.user

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.braodcast.AlertReceiver
import konkuk.gdsc.memotion.databinding.FragmentUserBinding
import konkuk.gdsc.memotion.domain.entity.user.User
import konkuk.gdsc.memotion.util.TAG
import konkuk.gdsc.memotion.util.calendarToString
import konkuk.gdsc.memotion.util.calendarToStringNoti


class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding: FragmentUserBinding
        get() = requireNotNull(_binding) { "UserFragment's binding is null" }
    private val viewModel: UserViewModel by viewModels()
    private val requiredPermission = arrayOf(
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.SCHEDULE_EXACT_ALARM
    )
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { isGranteds: Map<String, Boolean> ->
            for (permission in requiredPermission) {
                if(isGranteds.get(permission) != true) {
                    Log.d(TAG, "UserFragment - () called\n${permission} 허용안됨")
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userObserver = Observer<User> {
            binding.tvUserName.text = it.name
            Glide.with(this)
                .load(it.imageUrl)
                .centerCrop()
                .into(binding.ivUserProfileImage)
        }

        val notificationTimeObserver = Observer<Calendar> {
            if (binding.sUserReminder.isChecked) {
                Log.d(TAG, "UserFragment - onViewCreated() called\n변경됨")
                cancelAlarm()
                startAlarm(viewModel.notificationTime.value ?: Calendar.getInstance())
            }
            binding.tvUserReminderTime.text = calendarToStringNoti(it)
        }

        viewModel.profile.observe(viewLifecycleOwner, userObserver)
        viewModel.notificationTime.observe(viewLifecycleOwner, notificationTimeObserver)

        binding.apply {
            sUserReminder.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    if (!checkPermission()) {
                        Log.d(TAG, "UserFragment - onViewCreated() called\ncheckpermissoin 결과 실패")
                        /*requireActivity().requestPermissions(
                            arrayOf(
                                Manifest.permission.POST_NOTIFICATIONS,
                                Manifest.permission.SCHEDULE_EXACT_ALARM
                            ), 200
                        )*/
                        requestPermissionLauncher.launch(requiredPermission)
                        compoundButton.isChecked = false
                        return@setOnCheckedChangeListener
                    }
                    startAlarm(viewModel.notificationTime.value ?: Calendar.getInstance())
                    tvUserReminderTime.isEnabled = true
                    tvUserReminderTitle.isEnabled = true
                    ivUserReminderImage.setImageResource(R.drawable.icon_bell)
                } else {
                    cancelAlarm()
                    tvUserReminderTime.isEnabled = false
                    tvUserReminderTitle.isEnabled = false
                    ivUserReminderImage.setImageResource(R.drawable.icon_bell_un)
                }
            }

            tvUserReminderTime.setOnClickListener {
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        viewModel.updateNotificationTime(hour, minute)
                    }

                val cal = Calendar.getInstance()

                TimePickerDialog(
                    context,
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun startAlarm(c: Calendar) {
        Log.d(TAG, "UserFragment - startAlarm() called\n알람 시작점\n${calendarToString(c)}")
        if (!checkPermission()) {
            Log.d(TAG, "UserFragment - startAlarm() called\npermission 요청")
            requireActivity().requestPermissions(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.SCHEDULE_EXACT_ALARM
                ), 200
            )
            return
        }

        // AlarmManger 생성
        val alarmManager: AlarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireActivity(), AlertReceiver::class.java)

        val pendingIntent =
            PendingIntent.getBroadcast(requireContext(), 1, intent, PendingIntent.FLAG_IMMUTABLE)

        if (c.before(Calendar.getInstance())) {  // 설정된 시간이 현재 시간 이전인 경우 다음 날로 넘김
            c.add(Calendar.DATE, 1)
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
        Log.d(TAG, "UserFragment - startAlarm() called\n알람 끝점")
    }

    private fun cancelAlarm() {
        val alarmManager: AlarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireActivity(), AlertReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(requireContext(), 1, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
    }

    private fun checkPermission(): Boolean {
        Log.d(TAG, "UserFragment - checkPermission() called")
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.SCHEDULE_EXACT_ALARM
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

}