package dong.project.chart

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dong.project.chart.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.view
            .setData(mutableListOf(10, 15, 50, 20, 30, 10))
            .chartName("Bieu do cot luong mua")
            .columnColor(Color.RED)
            .verticalValue(mutableListOf("10", "11", "12", "1", "2"))
            .isValue(true)
    }
}