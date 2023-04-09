package com.example.myweatherforecastapplication.alert.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherforecastapplication.PreferenceHelper
import com.example.myweatherforecastapplication.PreferenceHelper.kindOfALERT
import com.example.myweatherforecastapplication.PreferenceHelper.language
import com.example.myweatherforecastapplication.PreferenceHelper.locationAlert
import com.example.myweatherforecastapplication.R
import com.example.myweatherforecastapplication.model.Alert
import com.example.myweatherforecastapplication.adapters.AlertAdapter.AlertAdapter
import com.example.myweatherforecastapplication.adapters.FavAdapter.SwipeItem
import com.example.myweatherforecastapplication.alert.viewmodel.AlertViewModel
import com.example.myweatherforecastapplication.alert.viewmodel.AlertViewModelFactory
import com.example.myweatherforecastapplication.db.LocalSource
import com.example.myweatherforecastapplication.homeScreen.view.CUSTOM_PREF_NAME
import com.example.myweatherforecastapplication.model.Repository
import com.example.myweatherforecastapplication.network.APIClient
import com.example.myweatherforecastapplication.utils.NetworkConnection
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Alert : Fragment(), OnClickListener, DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private lateinit var radioGroupForLocation: RadioGroup
    private lateinit var radioButtonForLocation: RadioButton
    private lateinit var radioGroupForAlarmChoice: RadioGroup
    private lateinit var radioButtonForAlarmChoice: RadioButton
    private lateinit var alertRecyclerView: RecyclerView
    private lateinit var alertAdapter: AlertAdapter
    private var alertList: MutableList<Alert>? = mutableListOf()
    private lateinit var alertViewModel: AlertViewModel
    private lateinit var alertViewModelFactory: AlertViewModelFactory
    private lateinit var addButton: Button
    private lateinit var startDateButton: Button
    private lateinit var endDateButton: Button
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var hasNetworkConnection: NetworkConnection
    private lateinit var connectionGroup: Group
    private lateinit var noConnectionGroup: Group
    private lateinit var dialog: Dialog
    private lateinit var prefs: SharedPreferences
    private lateinit var selectedLocationForAlert: String
    private lateinit var selectedNotificationForAlert: String
    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0
    var unixTime = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = PreferenceHelper.customPreference(requireContext(), CUSTOM_PREF_NAME)
        selectedLocationForAlert = getString(R.string.gps)
        selectedNotificationForAlert = getString(R.string.notification)
        alertViewModelFactory = AlertViewModelFactory(
            (Repository.getInstance(
                APIClient.getInstance(),
                LocalSource.getInstance(requireContext())
            ))
        )
        alertViewModel =
            ViewModelProvider(this, alertViewModelFactory).get(AlertViewModel::class.java)
        alertAdapter = AlertAdapter(requireContext())
        hasNetworkConnection = NetworkConnection.getInstance(requireContext())
        dialog = Dialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_alert, container, false)
        initializeUI(view)
        lifecycleScope.launch {
            alertViewModel.alerts.collect() {
                alertList=it as MutableList<Alert>
                alertAdapter.submitList(it)
            }
        }
        addButton.setOnClickListener {
            when (hasNetworkConnection.isOnline()) {
                true -> {
                    showDialog()
                    connectionGroup.visibility = View.VISIBLE
                    noConnectionGroup.visibility = View.GONE
                }
                false -> {
                    connectionGroup.visibility = View.GONE
                    noConnectionGroup.visibility = View.VISIBLE
                }
            }
        }
        lifecycleScope.launch {
            alertViewModel.alerts.collect {
                alertAdapter.submitList(it)
                swipeItemToDeleteIt()
            }
        }
        return view
    }

    override fun addAlertToDB(alert: Alert) {
        alertViewModel.insertAlertToDB(alert)
    }

    override fun removeAlertFromDB(alert: Alert) {
        alertViewModel.deleteAlertFromDB(alert)
    }

    private fun swipeItemToDeleteIt() {
        val swipeItem = object : SwipeItem(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = alertList?.get(viewHolder.adapterPosition)
                val position = viewHolder.adapterPosition
                val builder = AlertDialog.Builder(requireContext())
                alertList?.removeAt(viewHolder.adapterPosition)
                alertAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                builder.setMessage(R.string.sureToDeleteIt)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes) { dialog, id ->
                        if (deletedItem != null)
                            removeAlertFromDB(deletedItem)
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.no) { dialog, id ->
                        alertList?.add(position, deletedItem!!)
                        alertAdapter.notifyItemInserted(position)
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeItem)
        itemTouchHelper.attachToRecyclerView(alertRecyclerView)
    }

    private fun showDialog() {
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.show()
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        saveButton.setOnClickListener {
            prefs.locationAlert = selectedLocationForAlert
            prefs.kindOfALERT = selectedNotificationForAlert
            val alert = Alert(0, "", 12558L, 969999L, "", 0.0, 0.0)
            addAlertToDB(alert)
            dialog.dismiss()
        }
        startDateButton.setOnClickListener {
            showDatePicker()
            startDateButton.text = unixTime.toString()
        }
        endDateButton.setOnClickListener {
            showDatePicker()
            endDateButton.text = unixTime.toString()
        }
    }

    private fun setRadioButton() {
        radioGroupForLocation.setOnCheckedChangeListener { group, checkedId ->
            radioButtonForLocation = dialog.findViewById(checkedId)
            selectedLocationForAlert = radioButtonForLocation.text as String
        }

        radioGroupForAlarmChoice.setOnCheckedChangeListener { group, checkedId ->
            radioButtonForAlarmChoice = dialog.findViewById(checkedId)
            selectedNotificationForAlert = radioButtonForAlarmChoice.text as String
            if (selectedNotificationForAlert == getString(R.string.alert))
                checkPermission()
        }
    }

    private fun checkPermission() {
        if (!Settings.canDrawOverlays(requireContext())) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage(R.string.screenOverlay)
                .setCancelable(false)
                .setPositiveButton(R.string.yes) { dialog, id ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package: ${requireContext().applicationContext.packageName}")
                    )
                    startActivityForResult(intent, 1)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.no) { dialog, id ->
                    dialog.dismiss()
                    this.dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

    }

    private fun initializeUI(view: View) {
        alertRecyclerView = view.findViewById(R.id.alertRecyclerView)
        addButton = view.findViewById(R.id.add_button)
        connectionGroup = view.findViewById(R.id.connection_group)
        noConnectionGroup = view.findViewById(R.id.no_connection_group)
        dialog.setContentView(R.layout.popup_alarm_dialog)
        saveButton = dialog.findViewById(R.id.ok_button)
        cancelButton = dialog.findViewById(R.id.cancel_button)
        radioGroupForLocation = dialog.findViewById(R.id.locationChoice)
        radioGroupForAlarmChoice = dialog.findViewById(R.id.alarmChoice)
        startDateButton = dialog.findViewById(R.id.start_date_btn)
        endDateButton = dialog.findViewById(R.id.end_date_btn)
        setRadioButton()
        alertRecyclerView.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
                adapter = alertAdapter
            }
        }
    }

    private fun showDatePicker() {
        val calendar: Calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(requireContext(), this, year, month, day)
        datePickerDialog.show()


    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = day
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            requireContext(), this, hour, minute,
            DateFormat.is24HourFormat(requireContext())
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        val prefs = PreferenceHelper.customPreference(requireContext(), CUSTOM_PREF_NAME)
        val dateFormat = SimpleDateFormat("yyyyMMddHHmm", Locale(prefs.language))

        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"))
        try {
            unixTime = dateFormat.parse("$myMonth $myDay,$myYear")?.getTime() ?: 0L
            unixTime /= 1000L
        } catch (e: ParseException) {
            e.printStackTrace();
        }

    }
}