package br.com.app5m.skipitrestaurantes.helper

import android.content.Context
import android.util.Patterns
import android.widget.EditText
import br.com.app5m.skipitrestaurantes.R
import java.util.*

class ValidationHelper {
    fun shake(editText: EditText?) {
        if (editText != null) {
            Useful.shake(editText)
        }
    }

    companion object {
        /**
         * Created by Wesley Costa
         * On 30/04/2019 - 11/11/2019
         */
        private fun validatorCNPJ(CNPJ: String): Boolean {
            if (CNPJ == "00000000000000" || CNPJ == "11111111111111" || CNPJ == "22222222222222" || CNPJ == "33333333333333" || CNPJ == "44444444444444" || CNPJ == "55555555555555" || CNPJ == "66666666666666" || CNPJ == "77777777777777" || CNPJ == "88888888888888" || CNPJ == "99999999999999" || CNPJ.length != 14) return false
            val dig13: Char
            val dig14: Char
            var sm: Int
            var i: Int
            var r: Int
            var num: Int
            var peso: Int
            return try {
                sm = 0
                peso = 2
                i = 11
                while (i >= 0) {
                    num = CNPJ[i].toInt() - 48
                    sm = sm + num * peso
                    peso = peso + 1
                    if (peso == 10) peso = 2
                    i--
                }
                r = sm % 11
                dig13 = if (r == 0 || r == 1) '0' else (11 - r + 48).toChar()
                sm = 0
                peso = 2
                i = 12
                while (i >= 0) {
                    num = CNPJ[i].toInt() - 48
                    sm = sm + num * peso
                    peso = peso + 1
                    if (peso == 10) peso = 2
                    i--
                }
                r = sm % 11
                dig14 = if (r == 0 || r == 1) '0' else (11 - r + 48).toChar()
                dig13 == CNPJ[12] && dig14 == CNPJ[13]
            } catch (erro: InputMismatchException) {
                false
            }
        }

        private fun validatorCpf(CPF: String): Boolean {
            if (CPF == "00000000000" || CPF == "11111111111" || CPF == "22222222222" || CPF == "33333333333" || CPF == "44444444444" || CPF == "55555555555" || CPF == "66666666666" || CPF == "77777777777" || CPF == "88888888888" || CPF == "99999999999" || CPF.length != 11) return false
            val dig10: Char
            val dig11: Char
            var sm: Int
            var i: Int
            var r: Int
            var num: Int
            var peso: Int
            return try {
                sm = 0
                peso = 10
                i = 0
                while (i < 9) {
                    num = CPF[i].toInt() - 48
                    sm = sm + num * peso
                    peso = peso - 1
                    i++
                }
                r = 11 - sm % 11
                dig10 = if (r == 10 || r == 11) '0' else (r + 48).toChar()
                sm = 0
                peso = 11
                i = 0
                while (i < 10) {
                    num = CPF[i].toInt() - 48
                    sm = sm + num * peso
                    peso = peso - 1
                    i++
                }
                r = 11 - sm % 11
                dig11 = if (r == 10 || r == 11) '0' else (r + 48).toChar()
                dig10 == CPF[9] && dig11 == CPF[10]
            } catch (erro: InputMismatchException) {
                false
            }
        }

        fun phone(editText: EditText): Boolean {
            if (editText.getText().toString().isEmpty()) {
                error(editText, "Telefone deve ser preenchido.")
                Useful.shake(editText)
                return false
            }
            if (editText.getText().toString().length < 15) {
                error(editText, "Telefone deve ser preenchido de forma completa.")
                Useful.shake(editText)
                return false
            }
            return true
        }

        fun surname(editText: EditText): Boolean {


            val chars = editText.text

            val (letterOrDigitList, notLetterOrDigitList) = chars.partition { it.isLetterOrDigit() }
            println(letterOrDigitList) // [a, 1]
            println(notLetterOrDigitList) // [+]


            val (whitespaces, notWhitespaces) = chars.partition { it.isWhitespace() }

// whitespace char codes
            println(whitespaces.map(Char::code)) // [32, 9, 10, 160]
// non-whitespace chars
            println(notWhitespaces) // [1, a]

            if (editText.getText().toString().isEmpty()) {
                error(editText, "O Apelido deve ser preenchido.")
                Useful.shake(editText)
                return false
            }

            if (whitespaces.map(Char::code).isNotEmpty() ) {
                error(editText, "Não pode haver espaços no Apelido.")
                Useful.shake(editText)
                return false
            }
            if (notLetterOrDigitList.isNotEmpty()) {
                error(editText, "Remova os caracteres não alfanuméricos do Apelido.")
                Useful.shake(editText)
                return false
            }
            return true
        }



        fun cellphone(editText: EditText): Boolean {
            if (editText.getText().toString().isEmpty()) {
                error(editText, "Celular deve ser preenchido.")
                Useful.shake(editText)
                return false
            }
            if (editText.getText().toString().length < 14) {
                error(editText, "Celular deve ser preenchido de forma completa.")
                Useful.shake(editText)
                return false
            }
            return true
        }

        fun isEmpty(editText: EditText?, context: Context): Boolean {
            if (editText == null) {
                return false
            }
            if (editText.getHint() == null) {
                return !editText.getText().toString().isEmpty() || editText.getText()
                    .toString().length != 0
            }
            if (editText.getText().toString().isEmpty() && editText.getText()
                    .toString().length == 0
            ) {
                error(
                    editText,
                    editText.getHint().toString() + context.getString(R.string.must_filled_aut)
                )
                return false
            }
            return true
        }
        fun cep(editText: EditText, context: Context): Boolean {
            if (editText.getText().toString().isEmpty()) {
                error(editText, "O CEP não pode ser vazio.")
                Useful.shake(editText)
                return false
            }
            if (editText.getText().length < 8) {
                error(editText,"Digite todo o CEP.")
                Useful.shake(editText)
                return false
            }

            /*if (!Useful.strongPassword(editText.getText().toString(), 2)) {
            error(editText, "Senha deve conter pelo menos uma letra maiúscula");

            return;
        }
        if (!Useful.strongPassword(editText.getText().toString(), 3)) {
            error(editText, "Senha deve conter pelo menos uma letra minúscula");

            return;
        }*/return true
        }
        fun password(editText: EditText, context: Context): Boolean {
            if (editText.getText().toString().isEmpty()) {
                error(editText, context.getString(R.string.password_must_filled_aut))
                Useful.shake(editText)
                return false
            }
            if (editText.getText().length < 6) {
                error(editText, context.getString(R.string.password_must_least_digits))
                Useful.shake(editText)
                return false
            }
            if (!Useful.strongPassword(editText.getText().toString(), 1)) {
                error(editText, context.getString(R.string.password_must_contain_number))
                Useful.shake(editText)
                return false
            }
            /*if (!Useful.strongPassword(editText.getText().toString(), 2)) {
            error(editText, "Senha deve conter pelo menos uma letra maiúscula");

            return;
        }
        if (!Useful.strongPassword(editText.getText().toString(), 3)) {
            error(editText, "Senha deve conter pelo menos uma letra minúscula");

            return;
        }*/return true
        }

        /**
         * Caso o cpf não seja válido ele retorna falso
         */
        fun cpf(editText: EditText): Boolean {
            if (editText.getText().toString().isEmpty()) {
                error(editText, "CPF deve ser preenchido.")
                Useful.shake(editText)
                return false
            }
            if (editText.getText().length < 14) {
                error(editText, "CPF deve ser preenchido de forma completa.")
                Useful.shake(editText)
                return false
            }
            if (!validatorCpf(editText.getText().toString().replace(".", "").replace("-", ""))) {
                error(editText, "Este CPF não é válido.")
                Useful.shake(editText)
                return false
            }
            return true
        }

        private fun isValidEmail(target: CharSequence?): Boolean {
            return if (target == null) false else Patterns.EMAIL_ADDRESS.matcher(target)
                .matches()
        }

        fun cnpj(editText: EditText): Boolean {
            if (editText.getText().toString().isEmpty()) {
                error(editText, "CNPJ deve ser preenchido.")
                return false
            }
            if (editText.getText().length < 18) {
                error(editText, "CNPJ deve ser preenchido de forma completa.")
                return false
            }
            if (!validatorCNPJ(
                    editText.getText().toString().replace(".", "")
                        .replace("-", "").replace("/", "")
                )
            ) {
                error(editText, "Este CNPJ não é válido.")
                return false
            }
            return true
        }

        fun data(editText: EditText): Boolean {
            if (editText == null) {
                return false
            }
            if (editText.getText().toString().isEmpty()) {
                error(editText, "Data deve ser preenchida.")
                return false
            }
            if (editText.getText().toString().length < 10) {
                error(editText, "Data deve ser preenchida de forma completa.")
                return false
            }


            /*if (!Useful.dataValida(editText.getText().toString())) {
            error(editText, "Data deve estar em um formato válido.");
            return false;
        }*/return true
        }

        /**
         * Se o nome for válido ele retorna verdadeiro
         */
        fun name(editText: EditText, context: Context): Boolean {
            if (editText.getText().toString().isEmpty()) {
                error(editText, context.getString(R.string.name_must_filled_aut))
                Useful.shake(editText)
                return false
            }
            if (Useful.strongPassword(editText.getText().toString(), 1)) {
                error(editText, context.getString(R.string.name_contain_number))
                Useful.shake(editText)
                return false
            }
            if (!editText.getText().toString().contains(" ")) {
                error(editText, context.getString(R.string.enter_full_name))
                Useful.shake(editText)
                return false
            }
            if (editText.getText().toString().split(" ").toTypedArray().size <= 1) {
                error(editText, context.getString(R.string.enter_full_name))
                Useful.shake(editText)
                return false
            }
            return true
        }

        fun nickname(editText: EditText, context: Context): Boolean {
            if (editText.getText().toString().isEmpty()) {
                error(editText, context.getString(R.string.nickname_must_filled_aut))
                Useful.shake(editText)
                return false
            }
            //
//        if (Useful.strongPassword(editText.getText().toString(), 1)) {
//            error(editText, "Apelido não pode conter números");
//            Useful.shake(editText);
//            return false;
//        }

//
//        if (!editText.getText().toString().contains(" ")) {
//            error(editText, "Apelido deve ser preenchido de forma completa.");
//            Useful.shake(editText);
//            return false;
//        }

//        if (editText.getText().toString().split(" ").length <= 1) {
//            error(editText, "Nome deve ser preenchido de forma completa.");
//            Useful.shake(editText);
//            return false;
//        }
            return true
        }





        fun email(editText: EditText, context: Context): Boolean {
            if (editText.getText().toString().isEmpty()) {
                error(editText, context.getString(R.string.email_filled_aut))
                Useful.shake(editText)
                return false
            }
            if (!isValidEmail(editText.getText().toString())) {
                error(editText, context.getString(R.string.email_incorrect))
                Useful.shake(editText)
                return false
            }
            return true
        }

        fun coPassword(pass1: EditText, pass2: EditText, context: Context): Boolean {
            if (pass1.getText().toString() != pass2.getText().toString()) {
                error(pass2, context.getString(R.string.password_not_match))
                return false
            }
            return true
        }

        private fun error(editText: EditText?, texto: String) {
            if (editText == null) {
                return
            }
            editText.setError(texto)
            editText.requestFocus()
        }

        fun validateTexField(editText: EditText, context: Context): Boolean {
            if (!isEmpty(editText, context) || editText.getError() != null) {
                Useful.shake(editText)
                return false
            }
            return true
        }
    }
}