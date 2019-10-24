package ru.abbysoft.rehearsapp.booking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.Consumer
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.time_slot.view.*
import ru.abbysoft.rehearsapp.R
import ru.abbysoft.rehearsapp.databinding.TimeSlotBinding
import ru.abbysoft.rehearsapp.model.TimeSlot
import ru.abbysoft.rehearsapp.util.loadUserAsync

class TimeSlotsAdapter(private val timeSlots: List<TimeSlot>,
                       private val onSlotClicked: Consumer<TimeSlotBinding> = Consumer {  })
    : RecyclerView.Adapter<TimeSlotsAdapter.TimeSlotVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<TimeSlotBinding>(inflater, R.layout.time_slot, parent, false)

        return TimeSlotVH(binding)
    }

    override fun getItemCount(): Int {
        return timeSlots.size
    }

    override fun onBindViewHolder(holder: TimeSlotVH, position: Int) {
        holder.bind(timeSlots[position])
    }

    inner class TimeSlotVH(private val binding: TimeSlotBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(slot: TimeSlot) {
            binding.root.slot_book_button.setOnClickListener { onSlotClicked.accept(binding) }
            binding.slot = slot

            if (slot.bookedBy != -1L) {
                loadUserInfo(slot.bookedBy)
            }
        }

        private fun loadUserInfo(userId: Long) {
            // TODO use cache
            val context = binding.root.context
            context.loadUserAsync(userId, Consumer {
                binding.bookedBy = "${it.firstName} ${it.lastName}"
                binding.executePendingBindings()
            })
        }
    }

}
