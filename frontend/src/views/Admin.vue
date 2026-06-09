<template>
  <div class="admin-container">
    <h2>Login Administrativo</h2>

    <input v-model="loginUsername" placeholder="Email ou usuario ADMIN" />
    <input type="password" v-model="loginSenha" placeholder="Senha" />

    <button @click="login">Entrar</button>

    <p v-if="erroLogin" class="mensagem-erro">{{ erroLogin }}</p>
  </div>
</template>

<script>
import api from '../service/api.js'

export default {
  data() {
    return {
      loginUsername: '',
      loginSenha: '',
      erroLogin: ''
    }
  },

  methods: {
    async login() {
      this.erroLogin = ''

      if (!this.loginUsername || !this.loginSenha) {
        this.erroLogin = 'Preencha login e senha.'
        return
      }

      try {
        const response = await api.post('/login', {
          username: this.loginUsername,
          password: this.loginSenha
        })

        localStorage.setItem('token', response.data.accesstoken)
        localStorage.setItem('logado', 'true')
        this.$router.push('/painel')
      } catch {
        this.erroLogin = 'Login ou senha incorretos.'
      }
    }
  }
}
</script>
