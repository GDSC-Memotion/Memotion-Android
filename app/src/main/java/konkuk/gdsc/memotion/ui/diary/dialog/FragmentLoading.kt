package konkuk.gdsc.memotion.ui.diary.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import konkuk.gdsc.memotion.R
import konkuk.gdsc.memotion.databinding.FragmentLoadingBinding

class FragmentLoading: Fragment() {
    private var _binding: FragmentLoadingBinding? = null
    private val binding: FragmentLoadingBinding
        get() = requireNotNull(_binding) { "FragmentLoading's binding is null" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            parentFragmentManager.beginTransaction()
                .replace(R.id.fl_writing_diary_cover, FragmentEmotionCheck())
                .commit()
        }, 5000)

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}