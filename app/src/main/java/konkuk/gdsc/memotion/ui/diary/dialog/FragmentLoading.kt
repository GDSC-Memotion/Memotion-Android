package konkuk.gdsc.memotion.ui.diary.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import konkuk.gdsc.memotion.databinding.FragmentLoadingBinding
import konkuk.gdsc.memotion.ui.diary.create.WritingDiaryViewModel
import konkuk.gdsc.memotion.util.TAG

@AndroidEntryPoint
class FragmentLoading(
    private val loading: () -> Unit
) : Fragment() {
    private var _binding: FragmentLoadingBinding? = null
    private val binding: FragmentLoadingBinding
        get() = requireNotNull(_binding) { "FragmentLoading's binding is null" }
    private val viewModel: WritingDiaryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "FragmentLoading - onCreateView() called")
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            loading()
        }, 5000)

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}