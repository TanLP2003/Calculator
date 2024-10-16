package com.example.caculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var resultOutput: TextView? = null
    private var lastDigit: Boolean = false
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.title = "LifeCycle"

        resultOutput = findViewById(R.id.resultOutput)
    }

    fun onDigit(view: View){
        if(resultOutput?.text.toString().length == 1 && resultOutput?.text.toString().startsWith("0")){
            resultOutput?.text = ""
        }
        resultOutput?.append((view as TextView).text)
        lastDigit = true
    }

    fun onClear(view: View){
        resultOutput?.text = "0"
        lastDigit = true
        lastDot = false
    }

    fun onDecimalPoint(view: View) {
        if (lastDigit && !lastDot) {
            resultOutput?.append(".")
            lastDigit = false 
            lastDot = true 
        }
    }
    
    fun onOperator(view: View) {
        resultOutput?.text?.let {
            if (lastDigit && !isOperatorAdded(it.toString())) {
                resultOutput?.append((view as TextView).text)
                lastDigit = false
                lastDot = false
            }
        }
    }

    fun onDelete(view: View){
        var resValue = resultOutput?.text.toString()
        if(resValue.length <= 1){
            resultOutput?.text = "0"
            lastDigit = false
            return
        }else {
            if(resValue.endsWith(".")){
                lastDot = false
                lastDigit = true
            }
        }
        resultOutput?.text = resValue.dropLast(1)
        if(resultOutput?.text.toString().endsWith(".")){
            lastDot = true
            lastDigit = false
        }
    }
    
    fun onEqual(view: View) {
        if (lastDigit) {
            var resValue = resultOutput?.text.toString()
            var prefix = ""
            try {
                if (resValue.startsWith("-")) {
                    prefix = "-"
                    resValue = resValue.substring(1);
                }
                when {
                    resValue.contains("/") -> {
                        val splitValue = resValue.split("/")

                        var one = splitValue[0]
                        val two = splitValue[1]

                        if (prefix.isNotEmpty()) {
                            one = prefix + one
                        }

                        resultOutput?.text = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())
                    }
                    resValue.contains("x") -> {
                        val splitValue = resValue.split("x")

                        var one = splitValue[0] // Value One
                        val two = splitValue[1] // Value Two

                        if (prefix.isNotEmpty()) {
                            one = prefix + one
                        }

                        resultOutput?.text = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())
                    }
                    resValue.contains("-") -> {
                        val splitValue = resValue.split("-")
                        var one = splitValue[0]
                        val two = splitValue[1] 

                        if (prefix.isNotEmpty()) { 
                            one = prefix + one
                        }
                        
                        resultOutput?.text = removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())
                    }
                    resValue.contains("+") -> {
                        val splitValue = resValue.split("+")

                        var one = splitValue[0] 
                        val two = splitValue[1] 

                        if (prefix.isNotEmpty()) {
                            one = prefix + one
                        }

                        resultOutput?.text = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())
                    }
                }
            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    private fun removeZeroAfterDot(result: String): String {
        var value = result

        if (result.contains(".0")) {
            value = result.substring(0, result.length - 2)
        }

        return value
    }

    private fun isOperatorAdded(value: String): Boolean {
        return if (value.startsWith("-")) {
            false
        } else {
            (value.contains("/")
                    || value.contains("*")
                    || value.contains("-")
                    || value.contains("+"))
        }
    }
}