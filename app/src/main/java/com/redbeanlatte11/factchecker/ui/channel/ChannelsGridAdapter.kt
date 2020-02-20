import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.databinding.ChannelGridItemBinding
import com.redbeanlatte11.factchecker.ui.channel.ChannelDiffCallback
import com.redbeanlatte11.factchecker.ui.channel.ChannelItemCheckedListener
import com.redbeanlatte11.factchecker.ui.channel.ChannelItemClickListener

class ChannelsGridAdapter(
    private val itemClickListener: ChannelItemClickListener,
    private val itemCheckedListener: ChannelItemCheckedListener? = null,
    private val isEditing: Boolean = false
) : ListAdapter<Channel, ChannelsGridAdapter.ViewHolder>(ChannelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, itemClickListener, itemCheckedListener, isEditing)
    }

    class ViewHolder private constructor(private val binding: ChannelGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Channel,
            itemClickListener: ChannelItemClickListener?,
            itemCheckedListener: ChannelItemCheckedListener?,
            isEditing: Boolean
        ) {
            binding.channel = item
            binding.itemClickListener = ChannelItemClickListener.invoke { channel ->
                itemClickListener?.onClick(channel)
                if (isEditing) binding.checkbox.isChecked = !binding.checkbox.isChecked
            }
            binding.itemCheckedListener = ChannelItemCheckedListener.invoke { channel, isChecked ->
                itemCheckedListener?.onCheckedChanged(channel, isChecked)
                if (isChecked) {
                    binding.imageViewThumbnail.alpha = 1.0f
                } else {
                    binding.imageViewThumbnail.alpha = 0.3f
                }
            }
            binding.isEditing = isEditing
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