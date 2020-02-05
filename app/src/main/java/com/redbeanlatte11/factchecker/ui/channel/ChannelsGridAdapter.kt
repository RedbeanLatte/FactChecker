import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.databinding.ChannelGridItemBinding
import com.redbeanlatte11.factchecker.ui.channel.ChannelDiffCallback
import com.redbeanlatte11.factchecker.ui.channel.ChannelItemClickListener

class ChannelsGridAdapter(
    private val itemClickListener: ChannelItemClickListener
) : ListAdapter<Channel, ChannelsGridAdapter.ViewHolder>(ChannelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, itemClickListener)
    }

    class ViewHolder private constructor(private val binding: ChannelGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Channel,
            itemClickListener: ChannelItemClickListener
        ) {
            binding.channel = item
            binding.itemClickListener = itemClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChannelGridItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}