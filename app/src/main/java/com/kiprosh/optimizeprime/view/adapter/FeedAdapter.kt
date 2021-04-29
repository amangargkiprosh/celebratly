package com.kiprosh.optimizeprime.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.Player
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.FragmentMyFeedItemBinding
import com.kiprosh.optimizeprime.helper.CommonCode
import com.kiprosh.optimizeprime.helper.DateTimeUtil
import com.kiprosh.optimizeprime.helper.PlayerStateCallback
import com.kiprosh.optimizeprime.model.OccurrencesResponse
import com.kiprosh.optimizeprime.model.User
import com.kiprosh.optimizeprime.view.adapter.PlayerViewAdapter.Companion.releaseRecycledPlayers
import java.util.*

class FeedAdapter(
    private val mContext: Context,
    private val user: User?,
    private var modelList: ArrayList<OccurrencesResponse>,
    private var isMyFeed: Boolean,
    private val onShareClickListener: OnShareClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), PlayerStateCallback {
    private val dateTimeUtil = DateTimeUtil()

    private var mItemClickListener: OnItemClickListener? =
        null

    fun updateList(modelList: ArrayList<OccurrencesResponse>) {
        this.modelList = modelList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): VideoPlayerViewHolder {
        val binding: FragmentMyFeedItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context), R.layout.fragment_my_feed_item, viewGroup, false
        )
        return VideoPlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        //Here you can fill your row view
        if (holder is VideoPlayerViewHolder) {
            val model = getItem(position)
            val genericViewHolder = holder

            if (!isMyFeed) {
                if (position == 0) {
                    CommonCode(mContext).loadUserProfileImage(
                        holder.binding.ivUserProfile,
                        user!!.profileUrl
                    )
                    holder.binding.llPic.visibility = View.VISIBLE
                    holder.binding.cvProfileInfo.visibility = View.VISIBLE
                    holder.binding.tvMyFeed.visibility = View.VISIBLE
                    if (user != null) {
                        holder.binding.tvName.text = user.fullName
                        holder.binding.tvEmail.text = user.email
                        holder.binding.tvDob.text = dateTimeUtil.changeDateFormat(user.dob)
                        holder.binding.tvDoj.text = dateTimeUtil.changeDateFormat(user.doj)
                    }
                    updateCards(model, holder.binding)
                } else {
                    holder.binding.llPic.visibility = View.GONE
                    holder.binding.cvProfileInfo.visibility = View.GONE
                    holder.binding.tvMyFeed.visibility = View.GONE
                    updateCards(model, holder.binding)
                }
                holder.binding.ivShareText.visibility = View.VISIBLE
                holder.binding.ivShareText.setOnClickListener { onShareClickListener.onShareClick("https://player.vimeo.com/external/481511608.hd.mp4?s=40bfbf85159679a2f69f1155f9ae4d6da357580b") }
            } else {
                holder.binding.llPic.visibility = View.GONE
                holder.binding.cvProfileInfo.visibility = View.GONE
                holder.binding.tvMyFeed.visibility = View.GONE
                holder.binding.ivShareText.visibility = View.GONE
                updateCards(model, holder.binding)
            }
            // send data to view holder
            genericViewHolder.onBind(model, holder)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        val position = holder.adapterPosition
        releaseRecycledPlayers(position)
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    private fun getItem(position: Int): OccurrencesResponse {
        return modelList[position]
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(
            view: View?,
            position: Int,
            model: OccurrencesResponse?
        )
    }

    interface OnShareClickListener {
        fun onShareClick(link: String)
    }

    inner class VideoPlayerViewHolder(val binding: FragmentMyFeedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(model: OccurrencesResponse, holder: RecyclerView.ViewHolder) {

            // handel on item click
            binding.root.setOnClickListener {
                mItemClickListener!!.onItemClick(
                    it,
                    adapterPosition,
                    model
                )
            }
            var videoPlayerUrl = model.finalVideoUrl
            if (videoPlayerUrl.isNullOrEmpty()) {
                videoPlayerUrl =
                    "https://user-images.githubusercontent.com/10561168/116468087-0fde9580-a88e-11eb-8057-caedb4e35160.mp4"
            }

            binding.apply {
                videoUrl = videoPlayerUrl
                callback = this@FeedAdapter
                index = adapterPosition
                executePendingBindings()
            }


        }
    }

    private fun updateCards(model: OccurrencesResponse, binding: FragmentMyFeedItemBinding) {
        binding.date.text = dateTimeUtil.changeDateFormat(model.startAt)
        if (model.eventType == "work_anniversary") {
            binding.content.text = model.title + "\n" + model.caption
        } else {
            binding.content.text = model.title
        }
    }

    override fun onVideoDurationRetrieved(duration: Long, player: Player) {
        Log.d("playvideo", "onVideoDurationRetrieved")
    }

    override fun onVideoBuffering(player: Player) {
        Log.d("playvideo", "onVideoBuffering")
    }

    override fun onStartedPlaying(player: Player) {
        Log.d("playvideo", "staaaart" + player.contentDuration)

    }

    override fun onFinishedPlaying(player: Player) {
        Log.d("playvideo", "onFinishedPlaying")
    }
}