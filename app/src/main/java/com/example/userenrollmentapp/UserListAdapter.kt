package com.example.userenrollmentapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.userenrollmentapp.databinding.CardUserLayoutBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class UserListAdapter(private val listener: OnClick): ListAdapter<User, UserListAdapter.ViewHolder>(UserListDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = CardUserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindItems(item)
    }
    inner class ViewHolder(private val binding: CardUserLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItems(item: User){
            binding.cardUserName.text = "${item.firstName!!.capitalize()} ${item.lastName!!.capitalize()}"
            var date = Calendar.getInstance()
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            try{
                date.time =  sdf.parse(item.dob)
            } catch (e: ParseException){
                e.printStackTrace()
            }

            val age = getAge(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH))
            binding.cardUserDetails.text = "${item.gender!!.capitalize()} | $age | ${item.homeTown!!.capitalize()}"
            Glide.with(context)
                .load(item.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.cardUserImage)
            binding.cardUserDelete.setOnClickListener{
                listener.userDeleteClickListener(item.id.toString())
            }
        }
    }

    // Utility Function
    open fun getAge(DOByear: Int, DOBmonth: Int, DOBday: Int): Int {
        var age: Int
        val calenderToday = Calendar.getInstance()
        val currentYear = calenderToday[Calendar.YEAR]
        val currentMonth = 1 + calenderToday[Calendar.MONTH]
        val todayDay = calenderToday[Calendar.DAY_OF_MONTH]
        age = currentYear - DOByear
        if (DOBmonth > currentMonth) {
            --age
        } else if (DOBmonth == currentMonth) {
            if (DOBday > todayDay) {
                --age
            }
        }
        return age
    }

    class UserListDiffCallback: DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    interface OnClick {
        fun userDeleteClickListener(userId: String)
    }
}



