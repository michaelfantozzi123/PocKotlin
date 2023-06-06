package com.example.pockotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class Chart : Fragment() {

    companion object {
        fun newInstance() = Chart()
    }

    private lateinit var viewModel: ChartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChartViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pieChart: PieChart = view.findViewById(R.id.pieChart)

        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(18f, "Green"))
        entries.add(PieEntry(26f, "Yellow"))
        entries.add(PieEntry(22f, "Red"))
        // More entries ...

        val pieDataSet = PieDataSet(entries, "Data")
        pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.invalidate() // refresh
    }

}