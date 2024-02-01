package konkuk.gdsc.memotion.ui.diary.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import konkuk.gdsc.memotion.databinding.FragmentEmotionCheckBinding

class FragmentEmotionCheck : Fragment() {

    private var _binding: FragmentEmotionCheckBinding? = null
    private val binding: FragmentEmotionCheckBinding
        get() = requireNotNull(_binding) { "FragmentEmotionCheck's binding is null" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmotionCheckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}