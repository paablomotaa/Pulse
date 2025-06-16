package com.pmgdev.pulse.network

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.OnDataPointListener
import com.google.android.gms.fitness.request.SensorRequest
import com.pmgdev.pulse.ui.fitness.FitnessScreenViewModel
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

/**
 *
 * GoogelFitManager
 *
 * Objeto para la gestión de los datos de Google fits.
 *
 */
object GoogleFitManager {
    private var stepSensorListener: OnDataPointListener? = null

    /**
     *
     * getStepsForDay
     *
     * Este método recoge los datos de pasos dependiendo del offset que se le pase.
     * En este caso es -1 porque queremos recoger los pasos de ayer.
     *
     * @param context
     * @param dayOffset
     * @param onStepsRead
     * @param onError
     *
     */

    fun getStepsForDay(context: Context, dayOffset: Long, onStepsRead: (Int) -> Unit, onError: (Exception) -> Unit) {
        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()

        val account = GoogleSignIn.getAccountForExtension(context, fitnessOptions)

        val endOfDay = ZonedDateTime.now(ZoneId.systemDefault()).plusDays(dayOffset + 1).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1 // Fin del día (un milisegundo antes de la medianoche del día siguiente)
        val startOfDay = ZonedDateTime.now(ZoneId.systemDefault()).plusDays(dayOffset).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val request = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .setTimeRange(startOfDay, endOfDay, TimeUnit.MILLISECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()

        Fitness.getHistoryClient(context, account)
            .readData(request)
            .addOnSuccessListener { response ->
                var totalSteps = 0
                for (bucket in response.buckets) {
                    for (dataSet in bucket.dataSets) {
                        for (dp in dataSet.dataPoints) {
                            totalSteps += dp.getValue(Field.FIELD_STEPS).asInt()
                        }
                    }
                }
                onStepsRead(totalSteps)
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }

    /**
     *
     * getCaloriesToday
     *
     * Este método recoge los datos tipo calorías del dia.
     *
     * @param context
     * @param onCaloriesRead
     * @param onError
     *
     */
    fun getCaloriesToday(context: Context, onCaloriesRead: (Float) -> Unit, onError: (Exception) -> Unit) {
        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
            .build()

        val account = GoogleSignIn.getAccountForExtension(context, fitnessOptions)

        val end = System.currentTimeMillis()
        val start = ZonedDateTime.now().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val request = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
            .setTimeRange(start, end, TimeUnit.MILLISECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()

        Fitness.getHistoryClient(context, account)
            .readData(request)
            .addOnSuccessListener { response ->
                var totalCalories = 0f
                for (bucket in response.buckets) {
                    for (dataSet in bucket.dataSets) {
                        for (dp in dataSet.dataPoints) {
                            val calories = dp.getValue(Field.FIELD_CALORIES).asFloat()
                            totalCalories += calories
                        }
                    }
                }
                onCaloriesRead(totalCalories)
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }

    /**
     *
     * registerStepSensor
     *
     * Metodo para registrar el sensor para detectar los pasos a tiempo real
     *
     * @param context
     * @param onStepUpdate
     * @param onError
     *
     */
    fun registerStepSensor(context: Context, onStepUpdate: (Int) -> Unit, onError: (Exception) -> Unit) {
        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()

        val account = GoogleSignIn.getAccountForExtension(context, fitnessOptions)
        Log.d("Entro aqui","Entro aqui")
        stepSensorListener = OnDataPointListener { dataPoint: DataPoint ->
            Log.d("Llego","Llego correctamente a onDataPoint")
            for (field in dataPoint.dataType.fields) {
                if (field.name == Field.FIELD_STEPS.name) {
                    val steps = dataPoint.getValue(field).asInt()
                    Log.d("Llego","Llego correctamente a onStepUpdate")
                    onStepUpdate(steps)
                }
            }
        }

        Fitness.getSensorsClient(context, account)
            .add(
                SensorRequest.Builder()
                    .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                    .setSamplingRate(10, TimeUnit.SECONDS)
                    .build(),
                stepSensorListener!!
            )
            .addOnSuccessListener {
                Log.d("RegisterSensor", "OK")
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }

    /**
     *
     * unregisterStepSensor
     *
     * Este método hace lo contrario. Desregistra el sensor de pasos.
     *
     * @param context
     *
     */

    fun unregisterStepSensor(context: Context) {
        if (stepSensorListener == null) {
            return
        }

        val fitnessOptions = FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()
        val account = GoogleSignIn.getAccountForExtension(context, fitnessOptions)

        Fitness.getSensorsClient(context, account)
            .remove(stepSensorListener!!)
            .addOnSuccessListener {
                stepSensorListener = null
            }
            .addOnFailureListener { e ->
                Log.d("Error","Error")
            }
    }
}