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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.braodcast.AlertReceiver
import konkuk.gdsc.memotion.databinding.FragmentUserBinding
import konkuk.gdsc.memotion.util.calendarToStringNoti


class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding: FragmentUserBinding
        get() = requireNotNull(_binding) { "UserFragment's binding is null" }
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar: Calendar = Calendar.getInstance()

        binding.apply {
            sUserReminder.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    startAlarm(calendar)
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

            tvUserReminderTime.setOnClickListener{
                val cal = Calendar.getInstance()

                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        calendar.apply {
                            set(Calendar.HOUR_OF_DAY, hour)
                            set(Calendar.MINUTE, minute)
                            set(Calendar.MILLISECOND, 0)
                        }

                        binding.tvUserReminderTime.text = calendarToStringNoti(calendar)
                    }

                TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun startAlarm(c: Calendar) {
        if (!checkPermission()) {
            requireActivity().requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.SCHEDULE_EXACT_ALARM), 200
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

    companion object {
        const val INTENT_TIME = "time"
    }
}