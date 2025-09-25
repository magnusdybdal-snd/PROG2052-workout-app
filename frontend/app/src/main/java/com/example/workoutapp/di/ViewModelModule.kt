import com.example.workoutapp.features.home.ExercisesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ExercisesViewModel(get()) }  // GetExercisesUseCase is injected
}
