package com.example.sumcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    // TextView used to display the input and output
    lateinit var txtInput: TextView
    lateinit var txtOutput: TextView
    // Represent whether the lastly pressed key is numeric or not
    var lastElem: Boolean = false

    // Represent that current state is in error or not
    var stateError: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    txtInput = findViewById(R.id.txtInput)
        txtOutput = findViewById(R.id.txtOutput)
}
    fun backspace(view: View) {
        val str = txtInput.text.toString()
        if (str.isNotEmpty()) {
            txtInput.text = str.substring(0, str.length - 1)
        }
    }

    // Append the Button.text to the TextView
    fun digit(view: View) {
        if (stateError) {
            // if error, replace with error
            txtInput.text = (view as Button).text
            stateError = false
        } else {
            txtInput.append((view as Button).text)
        }
        lastElem = true
    }

     // Append +,-,*,/ operators to the TextView
    fun operator(view: View) {
        if (lastElem && !stateError) {
            txtInput.append((view as Button).text)
            lastElem = false
        }
    }

    fun clear(view: View) {
        this.txtInput.text = ""
        this.txtOutput.text = ""
        lastElem = false
        stateError = false
    }

    fun equals(view: View) {
        // Calculates only if the last elements is a number
        if (lastElem && !stateError) {
            // Read the problem
            val txt = txtInput.text.toString()

            // Create an Expression (A class from exp4j library)
            val expression = ExpressionBuilder(txt).build()

            try {
                // Display result
                val result = expression.evaluate()
                txtOutput.text = result.toString()
                this.txtInput.text = ""
            } catch (ex: ArithmeticException) {
                // Display error
                txtOutput.text = "ERROR"
                stateError = true
                lastElem = false
            }
        }
    }
}


/*
    button1.setOnClickListener{appendOnClick(true, "1")}
    button2.setOnClickListener{appendOnClick(true, "2")}
    button3.setOnClickListener{appendOnClick(true, "3")}
    button4.setOnClickListener{appendOnClick(true, "4")}
    button5.setOnClickListener{appendOnClick(true, "5")}
    button6.setOnClickListener{appendOnClick(true, "6")}
    button7.setOnClickListener{appendOnClick(true, "7")}
    button8.setOnClickListener{appendOnClick(true, "8")}
    button9.setOnClickListener{appendOnClick(true, "9")}

    buttonAdd.setOnClickListener{appendOnClick(false, "+")}
    buttonBack.setOnClickListener{
        backspace()
    }
    buttonEquals.setOnClickListener{
        calculate()
    }


    fun appendOnClick(clear: Boolean, string: String) {
        if (clear) {
            txtInput.text = ""
            txtInput.append(string)
        }else {
            txtInput.append(txtOutput.text)
            txtInput.append(string)
            txtOutput.text = ""
        }
    }

    private fun calculate() {
            try {
                val input = ExpressionBuilder(txtInput.text.toString()).build()
                val output = input.evaluate()
                val longOutput = output.toLong()
                if (output == longOutput.toDouble()) {
                    txtOutput.text = longOutput.toString()
                } else {
                    txtOutput.text = output.toString()
                }
                txtInput.text = ""
            } catch (ex: ArithmeticException) {
                // Display error
                txtOutput.text = "ERROR"
            }
        }
    }

*/