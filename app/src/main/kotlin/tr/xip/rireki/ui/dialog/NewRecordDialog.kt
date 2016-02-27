package tr.xip.rireki.ui.dialog


import android.app.DatePickerDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.afollestad.materialdialogs.MaterialDialog
import io.realm.Realm
import io.realm.RealmResults
import tr.xip.rireki.R

import kotlinx.android.synthetic.main.dialog_new_record.view.*
import tr.xip.rireki.broadcast.RecordInRealmUpdateBroadcast
import tr.xip.rireki.ext.isEmpty
import tr.xip.rireki.model.Record
import tr.xip.rireki.model.RecordTitle
import tr.xip.rireki.model.RecordUnit
import java.text.SimpleDateFormat
import java.util.*

import tr.xip.rireki.ext.toSimpleDate
import tr.xip.rireki.ext.toStringArray
import tr.xip.rireki.ext.toTimestamp

class NewRecordDialog {
    var context: Context? = null

    val realm = Realm.getDefaultInstance()

    var titles: RealmResults<RecordTitle>
    var units: RealmResults<RecordUnit>
    var date: Long = Calendar.getInstance().toSimpleDate().toTimestamp()

    init {
        titles = realm.allObjects(RecordTitle::class.java)
        units = realm.allObjects(RecordUnit::class.java)
    }

    fun create(activity: AppCompatActivity): MaterialDialog {
        context = activity

        var dialog = MaterialDialog.Builder(activity)
                .title(R.string.dialog_new_record)
                .customView(R.layout.dialog_new_record, true)
                .positiveText(R.string.action_ok)
                .negativeText(R.string.action_cancel)
                .autoDismiss(false)
                .onPositive { dialog, action -> addRecord(dialog) }
                .onNegative { dialog, action -> dialog.dismiss() }
                .dismissListener { realm.close() }
                .build()

        var view = dialog.customView!!

        var titlesAdapter = ArrayAdapter(activity, R.layout.item_spinner_simple, titles.toStringArray())
        view.title.setAdapter(titlesAdapter)

        var unitsAdapter = ArrayAdapter(activity, R.layout.item_spinner_simple, units.toStringArray())
        view.unit.setAdapter(unitsAdapter)

        view.date.text = SimpleDateFormat("d MMMM y").format(System.currentTimeMillis())
        view.date.setOnClickListener {
            DatePickerDialogFragment(DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                date = Date(year - 1900, month, day).time
                view.date.text = SimpleDateFormat("d MMMM y").format(Date(year - 1900, month, day));
            }).show(activity.supportFragmentManager, "date_picker_dialog_fragment")
        }

        return dialog
    }

    private fun addRecord(dialog: MaterialDialog) {
        if (!checkFieldValidity(dialog)) return

        realm.beginTransaction()

        var record = realm.createObject(Record::class.java)
        record.title = realm.copyToRealmOrUpdate(RecordTitle(dialog.customView!!.title.text.toString()))
        record.quantity = dialog.customView!!.quantity.text.toString().toInt()
        record.unit = realm.copyToRealmOrUpdate(RecordUnit(dialog.customView!!.unit.text.toString()))
        record.date = date

        realm.commitTransaction()

        RecordInRealmUpdateBroadcast().send(context!!)

        dialog.dismiss()
    }

    private fun checkFieldValidity(dialog: MaterialDialog): Boolean {
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