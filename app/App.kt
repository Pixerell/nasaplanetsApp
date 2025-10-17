
import android.app.Application

@HiltAndro
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize things here if needed
    }
}