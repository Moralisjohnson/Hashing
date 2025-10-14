import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns

mapa_arquivos = {
    'resultado_hash_encadeado_multiplicativo1.csv': 'Lista encadeada/hash multiplicativo',
    'resultado_hash_encadeado_multiplicativo2.csv': 'Lista encadeada/hash multiplicativo',
    'resultado_hash_encadeado_multiplicativo3.csv': 'Lista encadeada/hash multiplicativo',
    'resultados_tabela_linear1.csv': 'Hash com resto da divisão',
    'resultados_tabela_linear2.csv': 'Hash com resto da divisão',
    'resultados_tabela_linear3.csv': 'Hash com resto da divisão',
    'resultados_hashquadratico1.csv': 'Sondagem quadratica',
    'resultados_hashquadratico2.csv': 'Sondagem quadratica',
    'resultados_hashquadratico3.csv': 'Sondagem quadratica'
}

dados_lista = []
for arquivo, nome_algoritmo in mapa_arquivos.items():
    try:
        dado = pd.read_csv(arquivo)
        dado['algoritmo'] = nome_algoritmo
        dados_lista.append(dado)
    except FileNotFoundError:
        print(f"AVISO: O arquivo '{arquivo}' não foi encontrado e será ignorado.")

# Concatena todos os dataframes da lista em um só
dados_completos = pd.concat(dados_lista, ignore_index=True)

mapa_de_cores = {
    'Lista encadeada/hash multiplicativo': 'royalblue',
    'Hash com resto da divisão': 'darkorange',
    'Sondagem quadratica':'forestgreen'
}


plt.figure(figsize=(12, 7)) # Tamanho da figura

# sns.barplot cria o gráfico de barras
# x define o que vai no eixo x, ou seja, tamanho do conjunto
# y define o que vai no eixo y, ou seja, o tempo de insercao
# hue: a variável que vai definir a cor e agrupar as barras
# data: o dataframe completo
sns.barplot(x='tamanho do conjunto', y='tempo de inserção (ms)', hue='algoritmo', data=dados_completos, palette= mapa_de_cores)

# Adicionando títulos e rótulos
plt.title('Comparativo de Tempo de Inserção por Algoritmo de Hash', fontsize=16)
plt.ylabel("Tempo para Inserir (ms)", fontsize=12)
plt.xlabel("Tamanho do Conjunto/Quantidade de registros", fontsize=12)
plt.xticks(rotation=45) # Rotaciona os rótulos do eixo X se forem muitos
plt.grid(axis='y', linestyle='--', alpha=0.7)
plt.tight_layout() # Ajusta o layout

# Salvar o grafico
plt.savefig("Grafico_insercao.png")


plt.figure(figsize=(12, 7)) # Tamanho da figura

# sns.barplot cria o gráfico de barras
# x define o que vai no eixo x, ou seja, tamanho do conjunto
# y define o que vai no eixo y, ou seja, o tempo de insercao
# hue: a variável que vai definir a cor e agrupar as barras
# data: o dataframe completo
sns.barplot(x='tamanho do conjunto', y='tempo de busca (ms)', hue='algoritmo', data=dados_completos, palette= mapa_de_cores)

# Adicionando títulos e rótulos
plt.title('Relação de tempo de busca por algoritmo Hash', fontsize=16)
plt.ylabel("Tempo de buscar (ms)", fontsize=12)
plt.xlabel("Tamanho do conjunto/Quantidade de registros", fontsize=12)
plt.grid(axis='y', linestyle='--', alpha=0.7)
plt.tight_layout()

# Salvar o grafico
plt.savefig("Grafico_busca.png")



plt.figure(figsize=(12, 7)) # Tamanho da figura

# sns.barplot cria o gráfico de barras
# x define o que vai no eixo x, ou seja, tamanho do conjunto
# y define o que vai no eixo y, ou seja, o tempo de insercao
# hue: a variável que vai definir a cor e agrupar as barras
# data: o dataframe completo
sns.barplot(x='tamanho do conjunto', y=' colisoes', hue='algoritmo', data=dados_completos, palette= mapa_de_cores)

# Adicionando títulos e rótulos
plt.title('Quantidade de colisões por função e quantidade de registros', fontsize=16)
plt.ylabel("Colisões", fontsize=12, )
plt.xlabel("Tamanho do conjunto/Quantidade de registros", fontsize=12)
plt.grid(axis='y', linestyle='--', alpha=0.7)
plt.tight_layout()

# Salvar o grafico
plt.savefig("Grafico_colisoes.png")

#tabela
fig, ax = plt.subplots(figsize=(12,7))
ax.axis('off')

tabela = ax.table(cellText = dados_completos.values, colLabels = dados_completos.columns, loc = 'center', cellLoc = 'center')

for i in range(len(dados_completos.columns)):
    # Pega a célula do cabeçalho (linha 0, coluna i)
    celula_cabecalho = tabela[0, i]
    
    # Deixa o fundo da célula cinza claro
    celula_cabecalho.set_facecolor('#E0E0E0')
    celula_cabecalho.get_text().set_weight('bold')

tabela.auto_set_font_size(False)
tabela.set_fontsize(10)
tabela.auto_set_column_width(col=list(range(len(dados_completos.columns))))
plt.savefig('tabela_de_dados.png')

plt.show()