    package learning.com.manageralarm

    import android.app.AlarmManager
    import android.app.DatePickerDialog
    import android.app.PendingIntent
    import android.app.TimePickerDialog
    import android.content.Context
    import android.content.Intent
    import android.support.v7.app.AppCompatActivity
    import android.os.Bundle
    import android.view.View
    import android.widget.Button
    import android.widget.DatePicker
    import android.widget.TimePicker
    import android.widget.Toast
    import kotlinx.android.synthetic.main.activity_main.*
    import java.text.SimpleDateFormat
    import java.util.*

    const val EXTRA_PESAN = "EXTRA_PESAN"
    class MainActivity : AppCompatActivity() {
        var button_date : Button? = null
        private var cal = Calendar.getInstance()

        private fun setMyTimeFormat() : String{
            val myFormat = "HH:mm"
            val sdf = SimpleDateFormat(myFormat)
            return sdf.format(cal.time)
        }
        private fun myTimePicker() : TimePickerDialog.OnTimeSetListener{
            val timeSetListener = object : TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(p0: TimePicker?,
                                       hour: Int,
                                       minute: Int){
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    timeAlarm.text = setMyTimeFormat()
                }
            }
            return timeSetListener;
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            button_date = this.showDatePicker

            var mAlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val requestCode = 100
            var mPendingIntent : PendingIntent? = null
            setAlarm.setOnClickListener{
                var setTime = Calendar.getInstance()
                var dateTime = timeAlarm.text.split(":")
                var date =  dateAlarm.text.split("/")
                setTime.set(Calendar.HOUR_OF_DAY,dateTime[0].toInt())
                setTime.set(Calendar.MINUTE,dateTime[1].toInt())
                setTime.set(Calendar.SECOND,0)
                setTime.set(Calendar.YEAR,date[2].toInt())
                setTime.set(Calendar.MONTH,date[1].toInt())
                setTime.set(Calendar.DAY_OF_MONTH,date[0].toInt())

                val sendIntent = Intent(this, MyAlarmReceiver::class.java)
                sendIntent.putExtra(EXTRA_PESAN, myMessage.text.toString())
                mPendingIntent = PendingIntent.getBroadcast(this, requestCode, sendIntent, 0)
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, setTime.timeInMillis, mPendingIntent)
                Toast.makeText(this, "Alarm Manager dibuat untuk ${dateAlarm.text} ${timeAlarm.text}", Toast.LENGTH_SHORT).show()
            }
            cancelAlarm.setOnClickListener{
                if(mPendingIntent!=null){
                    mAlarmManager.cancel(mPendingIntent)
                    Toast.makeText(this, "Alarm Manager dibatalkan", Toast.LENGTH_SHORT).show()
                }
            }
            showTimePicker.setOnClickListener{ TimePickerDialog(
                this, myTimePicker(),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()}

            val dateSetListener = object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0 : DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int){
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateinView()
                }
            }

            button_date!!.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    DatePickerDialog(this@MainActivity, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
                }
            })

        }

        private fun updateDateinView() {
            val myFormat = "dd/M/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            dateAlarm!!.text = sdf.format(cal.getTime())
        }
    }
