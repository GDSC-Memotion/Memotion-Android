package konkuk.gdsc.memotion.ui.diary

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import konkuk.gdsc.memotion.domain.entity.diary.DiarySimple
import konkuk.gdsc.memotion.databinding.FragmentDiaryBinding
import konkuk.gdsc.memotion.util.TAG
import konkuk.gdsc.memotion.util.dpToPx
import konkuk.gdsc.memotion.util.view.setOnSingleClickListener

@AndroidEntryPoint
class DiaryFragment
    : Fragment(), DiaryAdapter.CalendarViewHolder.DateChangeListener, DiaryAdapter.DeleteDiaryListener {

    private var _binding: FragmentDiaryBinding? = null
    private val binding: FragmentDiaryBinding
        get() = requireNotNull(_binding) { "DiaryFragment's binding is null" }
    private val viewModel: DiaryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val diaryAdapter = DiaryAdapter(requireActivity(), DiarySimple.sample, this)

        val currentDayObserver = Observer<Calendar> {
            viewModel.getDailyDiary(dayConverter.format(viewModel.currentDay.value))
            viewModel.getMonthlyDiary(monthConverter.format(viewModel.currentDay.value))
        }
        val monthlyDiaryObserver = Observer<List<String>> {
//            var emotions = viewModel.getMonthlyDiary(monthConverter.format(viewModel.currentDay.value))
//            Log.d(TAG, "onViewCreated: $emotions")
//            if (emotions != null) {
//                diaryAdapter.updateMonth(emotions)
//            }
            diaryAdapter.updateMonth(it)
        }
        val dailyDiaryObserver = Observer<List<DiarySimple>> {
            diaryAdapter.updateData(it)
        }

        viewModel.currentDay.observe(viewLifecycleOwner, currentDayObserver)
        viewModel.monthlyDiary.observe(viewLifecycleOwner, monthlyDiaryObserver)
        viewModel.dailyDiary.observe(viewLifecycleOwner, dailyDiaryObserver)

        binding.apply {
            rvDiaryList.adapter = diaryAdapter

            rvDiaryList.setOnScrollChangeListener { view, _, _, _, _ ->
                if (!view.canScrollVertically(-1)) {
                    fabUp.visibility = View.GONE
                } else {
                    fabUp.visibility = View.VISIBLE
                }
            }

            fabUp.setOnSingleClickListener {
                rvDiaryList.smoothScrollToPosition(0)
            }
        }

        // 리사이클러뷰에 스와이프, 드래그 기능 달기
        val swipeHelperCallback = SwipeHelperCallback(diaryAdapter).apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            val size = dpToPx(requireActivity(), 51f)
            setClamp((resources.displayMetrics.widthPixels.toFloat() - size) / 6)    // 1080 / 4 = 270
        }

        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.rvDiaryList)

        // 다른 곳 터치 시 기존 선택했던 뷰 닫기
        binding.rvDiaryList.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(binding.rvDiaryList)
            false
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        val dayConverter = SimpleDateFormat("yyMMdd")
        val monthConverter = SimpleDateFormat("yyMM")
    }

    override fun dateChanged(year: Int, month: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month-1)
        cal.set(Calendar.DATE, dayOfMonth)
        viewModel.changeCurrentDay(cal)
        Log.d(TAG, "dateChanged: $year, $month, $dayOfMonth")
    }

    override fun deleteDiary(diaryId: Long) {
        viewModel.deleteDiary(diaryId)
    }

    override fun monthChanged(year: Int, month: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month-1)
        cal.set(Calendar.DATE, dayOfMonth)
    }
}