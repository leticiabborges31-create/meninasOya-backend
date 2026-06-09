<template>
  <div class="painel-container">
    <div class="painel-topo">
      <h2 class="titulo-painel">Painel Administrativo</h2>
      <button class="btn-sair" @click="logout">Sair</button>
    </div>

    <div class="secao">
      <div class="secao-topo">
        <h3>Criar Admin</h3>
        <button class="btn-toggle" @click="mostrarCriarAdmin = !mostrarCriarAdmin">
          {{ mostrarCriarAdmin ? 'Fechar' : 'Add Admin' }}
        </button>
      </div>

      <div v-if="mostrarCriarAdmin" class="formulario">
        <label>Email do novo admin</label>
        <input v-model="novoAdmin.email" type="email" placeholder="admin@email.com" />

        <label>Senha</label>
        <input v-model="novoAdmin.password" type="password" placeholder="Minimo 8 caracteres" />

        <div class="botoes">
          <button class="btn-publicar" @click="criarAdmin" :disabled="criandoAdmin">
            {{ criandoAdmin ? 'Criando...' : 'Criar Admin' }}
          </button>
        </div>

        <p v-if="sucessoAdmin" class="mensagem-sucesso">{{ sucessoAdmin }}</p>
        <p v-if="erroAdmin" class="mensagem-erro">{{ erroAdmin }}</p>
      </div>
    </div>

    <div class="secao">
      <h3>Adicionar Atividade</h3>

      <div class="formulario">
        <label>Titulo</label>
        <input v-model="titulo" type="text" placeholder="Digite o titulo da atividade" />

        <label>Descricao</label>
        <textarea v-model="descricao" placeholder="Digite a descricao da atividade"></textarea>

        <label>Data</label>
        <input v-model="dataAtividade" type="date" />

        <div class="botoes">
          <button class="btn-publicar" @click="publicar" :disabled="carregando">
            {{ carregando ? 'Publicando...' : 'Publicar' }}
          </button>
        </div>

        <div v-if="publicado" class="mensagem-sucesso">
          <p>Atividade publicada com sucesso.</p>
          <button class="btn-voltar" @click="$router.push('/')">
            Voltar ao inicio
          </button>
        </div>

        <p v-if="erro" class="mensagem-erro">{{ erro }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import api from '../service/api.js'

export default {
  data() {
    return {
      titulo: '',
      descricao: '',
      dataAtividade: '',
      publicado: false,
      carregando: false,
      erro: '',
      mostrarCriarAdmin: false,
      criandoAdmin: false,
      sucessoAdmin: '',
      erroAdmin: '',
      novoAdmin: {
        email: '',
        password: ''
      }
    }
  },

  methods: {
    async criarAdmin() {
      this.erroAdmin = ''
      this.sucessoAdmin = ''

      if (!this.novoAdmin.email || !this.novoAdmin.password) {
        this.erroAdmin = 'Preencha email e senha do novo admin.'
        return
      }

      this.criandoAdmin = true

      try {
        await api.post('/admins', {
          email: this.novoAdmin.email,
          password: this.novoAdmin.password
        })

        this.sucessoAdmin = 'Novo admin criado com sucesso.'
        this.novoAdmin.email = ''
        this.novoAdmin.password = ''
      } catch (error) {
        this.erroAdmin = error.response?.data?.message || 'Erro ao criar admin.'
      } finally {
        this.criandoAdmin = false
      }
    },

    async publicar() {
      this.erro = ''

      if (!this.titulo || !this.descricao || !this.dataAtividade) {
        this.erro = 'Preencha titulo, descricao e data.'
        return
      }

      this.carregando = true

      try {
        const formData = new FormData()
        formData.append('titulo', this.titulo)
        formData.append('descricao', this.descricao)
        formData.append('data', this.dataAtividade)

        await api.post('/atividades', formData)

        this.publicado = true
        this.titulo = ''
        this.descricao = ''
        this.dataAtividade = ''
      } catch (error) {
        this.erro = error.response?.data?.message || 'Erro ao publicar atividade.'
      } finally {
        this.carregando = false
      }
    },

    logout() {
      localStorage.removeItem('token')
      localStorage.removeItem('logado')
      this.$router.push('/admin')
    }
  }
}
</script>
