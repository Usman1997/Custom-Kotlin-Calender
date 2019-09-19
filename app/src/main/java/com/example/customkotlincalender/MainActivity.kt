package com.example.customkotlincalender

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import com.example.customkotlincalender.utils.next
import com.example.customkotlincalender.utils.previous
import com.example.customkotlincalender.utils.yearMonth
import com.example.model.CalendarDay
import com.example.model.DayOwner
import com.example.ui.DayBinder
import com.example.ui.ViewContainer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.calendar_day_legend.*
import kotlinx.android.synthetic.main.example_1_calendar_day.view.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val daysOfWeek = daysOfWeekFromLocale()
        legendLayout.children.forEachIndexed { index, view ->
            (view as TextView).apply {
                text = daysOfWeek[index].name.take(1).toUpperCase()
                setTextColorRes(R.color.example_1_white_light)
            }
        }
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(10)
        val endMonth = currentMonth.plusMonths(10)
        exOneCalendar.setup(startMonth, endMonth, daysOfWeek.first())
        exOneCalendar.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val textView = view.exOneDayText

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            exOneCalendar.notifyDateChanged(day.date)
                            oldDate?.let { exOneCalendar.notifyDateChanged(it) }
                            // updateAdapterForDate(day.date)
                        }
                    }
                }
            }
        }

        exOneCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.setTextColorRes(R.color.example_1_white)
                    textView.background = null
                    when (day.date) {
                        today -> {
                            textView.setTextColorRes(R.color.example_1_white)
                            textView.setBackgroundResource(R.drawable.example_1_today_bg)
                        }
                        selectedDate -> {
                            textView.setTextColorRes(R.color.example_1_bg)
                            textView.setBackgroundResource(R.drawable.example_1_selected_bg)
                        }
                        else -> {
                            textView.setTextColorRes(R.color.example_1_white)
                            textView.background = null
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.example_1_white_light)
                    textView.background = null

                }
            }
        }

        exOneCalendar.monthScrollListener = { month ->
            val title = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
            exFiveMonthYearText.text = title
        }
        exFiveNextMonthImage.setOnClickListener {
            exOneCalendar.findFirstVisibleMonth()?.let {
                exOneCalendar.smoothScrollToMonth(it.yearMonth.next)
            }
        }
        exFivePreviousMonthImage.setOnClickListener {
            exOneCalendar.findFirstVisibleMonth()?.let {
                exOneCalendar.smoothScrollToMonth(it.yearMonth.previous)
            }
        }
    }
}