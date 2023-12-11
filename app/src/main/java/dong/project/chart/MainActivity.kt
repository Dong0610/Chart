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
//        binding.view.setData(
//            mutableListOf(
//                CircularChart.ChartData(10f, "Name1"),
//                CircularChart.ChartData(20f, "Name2"),
//                CircularChart.ChartData(30f, "Name3"),
//                CircularChart.ChartData(10f, "Name1"),
//                CircularChart.ChartData(20f, "Name2"),
//                CircularChart.ChartData(30f, "Name3")
//            )
//
//        )
//            .chartName("Bieu do cot luong mua")
////            .columnColor(Color.RED)
////            .verticalValue(mutableListOf("1000","2000","3000","400","60"))
//            .setGloss(true)
////            .isValue(true)

        binding.view
            .setData(mutableListOf(10, 15, 50, 20, 30, 10))
            .chartName("Bieu do cot luong mua")
            .columnColor(Color.RED)
            .verticalValue(mutableListOf("1000", "2000", "3000", "400", "60"))
            .isValue(true)
//        binding.view
//            .setData(mutableListOf(10, 15, 50, 20, 30, 10))
//            .chartName("Bieu do cot luong mua")
//            .columnColor(Color.RED)
//            .verticalValue(mutableListOf("1000", "2000", "3000", "400", "60"))
//            .isValue(true)
    }
}