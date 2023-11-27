package dong.project.chart

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dong.project.chart.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.view.setData(mutableListOf(10, 30, 40, 10, 30, 42))
            .chartName("Bieu do cot luong mua").columnColor(Color.RED)
            .verticalValue(mutableListOf("1000","2000","3000","400","60"))
            .setGloss(true,"mm/thang")
            .isValue(true)

    }
}