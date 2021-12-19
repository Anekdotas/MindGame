package anekdotas.mindgameapplication


import anekdotas.mindgameapplication.objects.RegistrationUtil
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {
    @Test
    fun `incorrect password confirmation returns false`(){
        val result = RegistrationUtil.validateRegistrationInput(
            "Josh",
            "asd123",
            "asdf123",
            "Josh@gmail.com"
        )
        assertThat(result).isFalse()
    }
}