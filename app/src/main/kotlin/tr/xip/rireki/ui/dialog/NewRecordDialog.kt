package tr.xip.rireki.ui.dialog


import android.app.DatePickerDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.afollestad.materialdialogs.MaterialDialog
import io.realm.Realm
import io.realm.RealmResults
import tr.xip.rireki.R

import kotlinx.android.synthetic.main.dialog_record.view.*
import tr.xip.rireki.event.Bus
import tr.xip.rireki.event.RecordUpdatedEvent
import tr.xip.rireki.ext.*
import tr.xip.rireki.model.Record
import tr.xip.rireki.model.RecordTitle
import tr.xip.rireki.model.RecordUnit
import tr.xip.rireki.realm.saveRecordToRealm
import java.text.SimpleDateFormat
import java.util.*

object NewRecordDialog {
    private val MODE_NEW = 0
    private val MODE_ADD = 1
    private val MODE_EDIT = 2

    private var context: Context? = null

    private val realm = Realm.getDefaultInstance()

    private var titles: RealmResults<RecordTitle>
    private var units: RealmResults<RecordUnit>
    private var date: Long = Calendar.getInstance().toSimpleDate().toTimestamp()

    init {
        titles = realm.allObjects(RecordTitle::class.java)
        units = realm.allObjects(RecordUnit::class.java)
    }

    fun new(activity: AppCompatActivity): MaterialDialog = create(activity = activity, mode = MODE_NEW)

    fun addMore(activity: AppCompatActivity, record: Record): MaterialDialog = create(activity = activity, title = record.title?.title, unit = record.unit?.unit, mode = MODE_ADD)

    fun edit(activity: AppCompatActivity, record: Record): MaterialDialog {
        return create(activity, record.title?.title, record.quantity, record.unit?.unit, record.date, MODE_EDIT)
    }

    private fun create(activity: AppCompatActivity, title: String? = null, quantity: Int? = null, unit: String? = null, date: Long? = null, mode: Int): MaterialDialog {
        context = activity

        var dialogTitle: Int = R.string.dialog_add_more
        when (mode) {
            MODE_NEW -> dialogTitle = R.string.dialog_new_record
            MODE_ADD -> dialogTitle = R.string.dialog_add_more
            MODE_EDIT -> dialogTitle = R.string.dialog_edit_record
        }
        var dialog = MaterialDialog.Builder(activity)
                .title(dialogTitle)
                .customView(R.layout.dialog_record, true)
                .positiveText(R.string.action_ok)
                .negativeText(R.string.action_cancel)
                .autoDismiss(false)
                .onPositive { dialog, action -> addRecord(dialog, mode) }
                .onNegative { dialog, action -> dialog.dismiss() }
                .dismissListener { realm.close() }
                .build()

        var view = dialog.customView!!

        var titlesAdapter = ArrayAdapter(activity, R.layout.item_spinner_simple, titles.toStringArray())
        view.title.setAdapter(titlesAdapter)
        view.title.setText(title ?: "")

        if (quantity != null) {
            view.quantity.setText("$quantity")
        }

        var unitsAdapter = ArrayAdapter(activity, R.layout.item_spinner_simple, units.toStringArray())
        view.unit.setAdapter(unitsAdapter)
        view.unit.setText(unit ?: "")

        view.date.text = SimpleDateFormat("d MMMM y").format(date ?: System.currentTimeMillis())
        view.date.setOnClickListener {
            DatePickerDialogFragment(DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                val cal = Calendar.getInstance().toSimpleDate()
                cal.set(year, month, day)
                this.date = cal.toTimestamp()
                view.date.text = SimpleDateFormat("d MMMM y").format(this.date);
            }).show(activity.supportFragmentManager, "date_picker_dialog_fragment")
        }

        if (mode == MODE_EDIT) {
            view.title.isEnabled = false
            view.quantity.requestFocus()
            view.unit.isEnabled = false
            view.date.isEnabled = false
            view.date.alpha = 0.7f
        }

        return dialog
    }

    private fun addRecord(dialog: MaterialDialog, mode: Int) {
        if (!validateFields(dialog)) return

        var record = Record()
        record.title = RecordTitle(dialog.customView!!.title.text.toString())
        record.quantity = dialog.customView!!.quantity.text.toString().toInt()
        record.unit = RecordUnit(dialog.customView!!.unit.text.toString())
        record.date = date

        val realm = Realm.getDefaultInstance()

        val existingRecord = realm.where(Record::class.java).equalTo("title.title", record.title?.title).findFirst()

        /* Update existing Record if it exists in Realm */
        if (existingRecord != null) {
            realm.beginTransaction()
            if (mode == MODE_ADD) {
                existingRecord.quantity = existingRecord.quantity ?: 0 + record.quantity as Int
            } else {
                existingRecord.quantity = record.quantity
            }
            existingRecord.date = record.date
            realm.commitTransaction()
            Bus.post(RecordUpdatedEvent(existingRecord))
        } else {
            saveRecordToRealm(record)
        }

        dialog.dismiss()
    }

    private fun validateFields(dialog: MaterialDialog): Boolean {
        var result = true
        val view = dialog.customView!!
        if (view.title.isEmpty()) {
            view.titleWrap.error = context!!.getString(R.string.error_title_field_cant_be_empty)
            result = false
        }
        if (view.quantity.isEmpty()) {
            view.quantityWrap.error = context!!.getString(R.string.error_quantity_field_cant_be_empty)
            result = false
        }
        if (view.unit.isEmpty()) {
            view.unitWrap.error = context!!.getString(R.string.error_unit_field_cant_be_empty)
            result = false
        }
        return result
    }
}