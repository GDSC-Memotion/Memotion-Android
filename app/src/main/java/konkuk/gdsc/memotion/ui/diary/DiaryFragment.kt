package konkuk.gdsc.memotion.ui.diary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import konkuk.gdsc.memotion.data.DiarySimple
import konkuk.gdsc.memotion.databinding.FragmentDiaryBinding
import konkuk.gdsc.memotion.util.dpToPx

class DiaryFragment : Fragment() {

    private var _binding: FragmentDiaryBinding? = null
    private val binding: FragmentDiaryBinding
        get() = requireNotNull(_binding) { "DiaryFragment's binding is null" }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val diaryAdapter = DiaryAdapter(requireActivity(), DiarySimple.sample)

        binding.apply {
            rvDiaryList.adapter = diaryAdapter
            rvDiaryList.layoutManager = LinearLayoutManager(requireActivity())
        }

        // 리사이클러뷰에 스와이프, 드래그 기능 달기
        val swipeHelperCallback = SwipeHelperCallback(diaryAdapter).apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            val size = dpToPx(requireActivity(), 51f)
            setClamp((resources.displayMetrics.widthPixels.toFloat() - size) / 4)    // 1080 / 4 = 270
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
}