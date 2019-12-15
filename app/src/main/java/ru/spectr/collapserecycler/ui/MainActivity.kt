package ru.spectr.collapserecycler.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.spectr.collapserecycler.R


class MainActivity : AppCompatActivity(), MainActivityView {
    //TODO: inject fields
    private val presenter = MainActivityPresenter()
    private val adapter = CollapseAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.attach(this)
        cbSelectAll.setOnClickListener { presenter.onSelectAllClick() }
        tvContactName.setOnClickListener { presenter.onContactNameClick() }
        tvContactPhone.setOnClickListener { presenter.onContactPhoneClick() }
        adapter.onContactSelected = presenter::onContactSelected
        adapter.onSubGroupSelected = presenter::onSubGroupSelected
        adapter.onGroupSelected = presenter::onGroupSelected
        btNext.setOnClickListener { presenter.onNextButtonClick() }
    }

    override fun setContacts(groups: List<Group>) {
        adapter.setData(groups)
        rvContacts.adapter = adapter
    }

    override fun setAllSelected(checked: Boolean) {
        cbSelectAll.isChecked = checked
    }

    override fun updateContacts() {
        rvContacts.adapter?.notifyDataSetChanged()
    }

    override fun setNameSortState(descending: Boolean) {
        val icon = if (descending) R.drawable.arrow_up else R.drawable.arrow_down
        tvContactName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, icon, 0)
    }

    override fun setPhoneSortState(descending: Boolean) {
        val icon = if (descending) R.drawable.arrow_down else R.drawable.arrow_up
        tvContactPhone.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, icon, 0)
    }

    override fun showNextButton(show: Boolean) {
        btNext.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun updateElement(element: RecyclerElement) {
        adapter.updateElement(element)
    }
}
