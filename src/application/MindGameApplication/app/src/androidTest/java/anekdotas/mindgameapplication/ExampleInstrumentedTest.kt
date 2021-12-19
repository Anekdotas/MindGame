package anekdotas.mindgameapplication

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test


// Tests for MainActivity
class MainActivityInstrumentationTest {
    // Preferred JUnit 4 mechanism of specifying the activity to be launched before each test
    @Rule @JvmField
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    // Looks for an EditText with id = "R.id.username"
    // Types the text "Hello" into the EditText
    // Verifies the EditText has text "Hello"
    @Test
    fun validateEditText() {
        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("Hello"))
            .check(matches(withText("Hello")))
    }
}