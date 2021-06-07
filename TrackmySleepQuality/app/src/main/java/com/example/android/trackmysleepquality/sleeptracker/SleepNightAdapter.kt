package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding
import com.example.android.trackmysleepquality.generated.callback.OnClickListener

class SleepNightAdapter(private val clickListener:SleepNightListener) : ListAdapter<SleepNight,
        SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {

    // ViewHolder 만 변경해서 다른 로직으로 바꿀 수 있게 캡슐화 됨
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    /**
     * private constructor 로 만들어서 class 내부에서만 호출가능하도록 만듦
    **/
    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

        // RecyclerView는 여러 타입의 ViewHolder를 지원할 수 있기 때문에 캡슐화
        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            binding.sleep = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

/**
 * List 내부의 두 개의 Item 사이의 차이점을 계산하는 Callback
 *
 * ListAdapter 는 new Item 과 old Item이 얼마나 변했는지 최소 개수를 알아내기 위해 사용
**/
class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }
}

/**
 * click 을 감지하고 클릭과 관련되어 처리해야 데이터를 fragment 에 전달합니다.
 * click event 가 발생할 때마다 이를 fragment 에 알리는 아래의 Callback method 는 ViewHolder 가 가지고 있을 것입니다.
 *
 * nightId만 있어도 Database 에서 원하는 데이터에 접근할 수 있기 때문에
 * SleepNight 객체 참조를 가지고 있을 필요는 없습니다. 따라서 sleep night id 만 파라미터로 넘겨줍니다.
 **/
class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}