package ru.spectr.collapserecycler.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_contact.view.*
import kotlinx.android.synthetic.main.item_contact_group.view.*
import kotlinx.android.synthetic.main.item_contact_subgroup.view.*
import ru.spectr.collapserecycler.R

class CollapseAdapter : RecyclerView.Adapter<CollapseAdapter.ViewHolder>() {
    private val recyclerModel: MutableList<RecyclerElement> = ArrayList()
    lateinit var groups: List<Group>

    lateinit var onGroupSelected: (group: Group) -> Unit
    lateinit var onSubGroupSelected: (subgroup: SubGroup) -> Unit
    lateinit var onContactSelected: (contact: Contact) -> Unit

    fun setData(data: List<Group>) {
        groups = data
        generateModel()
        notifyDataSetChanged()
    }

    private fun generateModel() {
        recyclerModel.clear()
        for (group in groups) {
            recyclerModel.add(group)
            if (group.isExpanded) {
                for (subGroup in group.subGroups) {
                    recyclerModel.add(subGroup)
                    if (subGroup.isExpanded)
                        recyclerModel.addAll(subGroup.contacts)
                }
            }
        }
    }

    companion object {
        private const val TYPE_GROUP = 0
        private const val TYPE_SUBGROUP = 1
        private const val TYPE_CONTACT = 2
    }

    fun updateElement(element: RecyclerElement) {
        val pos = recyclerModel.indexOf(element)
        if (pos != -1)
            notifyItemChanged(pos)
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.cbCheck)

        init {
            checkBox.setOnClickListener {
                when (val element = recyclerModel[adapterPosition]) {
                    is Group -> {
                        element.setAllChecked(checkBox.isChecked)
                        if (element.isExpanded)
                            notifyItemRangeChanged(
                                adapterPosition + 1,
                                element.expandableElementsCount()
                            )
                        if (::onGroupSelected.isInitialized)
                            onGroupSelected(element)
                    }
                    is SubGroup -> {
                        element.setAllChecked(checkBox.isChecked)
                        if (element.isExpanded)
                            notifyItemRangeChanged(adapterPosition + 1, element.contacts.size)
                        if (::onSubGroupSelected.isInitialized)
                            onSubGroupSelected(element)
                    }
                    is Contact -> {
                        element.isChecked = checkBox.isChecked
                        if (::onContactSelected.isInitialized)
                            onContactSelected(element)
                    }
                }
                notifyItemChanged(adapterPosition)
            }
        }

        fun setChecked(checked: Boolean) {
            checkBox.isChecked = checked
        }
    }

    inner class GroupHolder(itemView: View) : ViewHolder(itemView) {
        init {
            itemView.tvGroupName.setOnClickListener {
                val group = recyclerModel[adapterPosition] as Group

                group.isExpanded = !group.isExpanded
                generateModel()

                if (group.isExpanded)
                    notifyItemRangeInserted(adapterPosition + 1, group.expandableElementsCount())
                else
                    notifyItemRangeRemoved(adapterPosition + 1, group.expandableElementsCount())

                notifyItemChanged(adapterPosition)
            }
        }

        fun setGroupName(name: String) {
            itemView.tvGroupName.text = name
        }

        fun setExpanded(expanded: Boolean) {
            val icon = if (expanded) R.drawable.arrow_down else R.drawable.arrow_left
            itemView.tvGroupName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, icon, 0)
        }
    }

    inner class SubGroupHolder(itemView: View) : ViewHolder(itemView) {
        init {
            itemView.tvSubGroupName.setOnClickListener {
                val subGroup = recyclerModel[adapterPosition] as SubGroup

                subGroup.isExpanded = !subGroup.isExpanded
                generateModel()

                if (subGroup.isExpanded)
                    notifyItemRangeInserted(adapterPosition + 1, subGroup.contacts.size)
                else
                    notifyItemRangeRemoved(adapterPosition + 1, subGroup.contacts.size)

                notifyItemChanged(adapterPosition)
            }
        }

        fun setSubGroupName(name: String) {
            itemView.tvSubGroupName.text = name
        }

        fun setExpanded(expanded: Boolean) {
            val icon = if (expanded) R.drawable.arrow_down else R.drawable.arrow_left
            itemView.tvSubGroupName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, icon, 0)
        }
    }

    inner class ContactHolder(itemView: View) : ViewHolder(itemView) {
        fun setContactPhone(phone: String) {
            itemView.tvContactPhone.text = phone
        }

        fun setContactName(name: String) {
            itemView.tvContactName.text = name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: (Int) -> View =
            { res -> LayoutInflater.from(parent.context).inflate(res, parent, false) }

        return when (viewType) {
            TYPE_GROUP -> GroupHolder(view(R.layout.item_contact_group))
            TYPE_SUBGROUP -> SubGroupHolder(view(R.layout.item_contact_subgroup))
            else -> ContactHolder(view(R.layout.item_contact))
        }
    }

    override fun getItemCount(): Int = recyclerModel.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = recyclerModel[position]
        //Using IF instead of WHEN for double smart cast
        if (element is Group && holder is GroupHolder) {
            holder.setGroupName(element.name)
            holder.setExpanded(element.isExpanded)
        }
        if (element is SubGroup && holder is SubGroupHolder) {
            holder.setSubGroupName(element.name)
            holder.setExpanded(element.isExpanded)
        }
        if (element is Contact && holder is ContactHolder) {
            holder.setContactName(element.name)
            holder.setContactPhone(element.phone)
        }
        holder.setChecked(element.isChecked)
    }

    override fun getItemViewType(position: Int): Int {
        return when (recyclerModel[position]) {
            is Group -> TYPE_GROUP
            is SubGroup -> TYPE_SUBGROUP
            is Contact -> TYPE_CONTACT
        }
    }
}