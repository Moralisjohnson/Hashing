import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns

mapa_arquivos = {
    'resultado_hash_encadeado_multiplicativo.csv': 'Lista encadeada/hash multiplicativo',
    'resultado_hash_encadeado_multiplicativo2.csv': 'Lista encadeada/hash multiplicativo',
    'resultado_hash_encadeado_multiplicativo3.csv': 'Lista encadeada/hash multiplicativo',
    'resultados_tabela_linear1.csv': 'Hash com resto da divisão',
    'resultados_tabela_linear2.csv': 'Hash com resto da divisão',
    'resultados_tabela_linear3.csv': 'Hash com resto da divisão',
    'resultados_hashquadratico.csv': 'Sondagem quadratica',
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


plt.figure(figsize=(12, 7)) # Tamanho da figura

# sns.barplot cria o gráfico de barras
# x define o que vai no eixo x, ou seja, tamanho do conjunto
# y define o que vai no eixo y, ou seja, o tempo de insercao
# hue: a variável que vai definir a cor e agrupar as barras
# data: o dataframe completo
sns.barplot(x='tamanho do conjunto', y='tempo de inserção (ms)', hue='algoritmo', data=dados_completos)

# Adicionando títulos e rótulos
plt.title('Comparativo de Tempo de Inserção por Algoritmo de Hash', fontsize=16)
plt.ylabel("Tempo para Inserir (ms)", fontsize=12)
plt.xlabel("Tamanho do Conjunto", fontsize=12)
plt.xticks(rotation=45) # Rotaciona os rótulos do eixo X se forem muitos
plt.grid(axis='y', linestyle='--', alpha=0.7)
plt.tight_layout() # Ajusta o layout

# Salvar o grafico
plt.savefig("Grafico_Comparativo_Hash.png")


plt.figure(figsize=(12, 7)) # Tamanho da figura

# sns.barplot cria o gráfico de barras
# x define o que vai no eixo x, ou seja, tamanho do conjunto
# y define o que vai no eixo y, ou seja, o tempo de insercao
# hue: a variável que vai definir a cor e agrupar as barras
# data: o dataframe completo
sns.barplot(x=' colisoes', y='TabelaHashLinear', hue='algoritmo', data=dados_completos)

# Adicionando títulos e rótulos
plt.title('Relação de colisão x Tabela Hash', fontsize=16)
plt.ylabel("Colisões", fontsize=12)
plt.xlabel("Tabela hash", fontsize=12)
plt.grid(axis='y', linestyle='--', alpha=0.7)
plt.tight_layout()

# Salvar o grafico
plt.savefig("Grafico_Colisões.png")

plt.show()