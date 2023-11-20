from django.http import HttpResponse
from keras.models import model_from_json
import numpy as np
from keras.models import Sequential
from keras.layers import Dense

# Función para asignar etiqueta de enfermedad (0-3) basada en la salida
def enfermedad(x,y):
    enfermedad = x*2 + y
    return f"enfermedad {enfermedad}"

def loadData():
    # Número de casos de entrenamiento
    num_cases = 20

    # Número de síntomas
    num_symptoms = 8

    # Generar valores iniciales para W y U dentro del rango (0.4, 0.9)
    initial_values_W = np.random.uniform(0.4, 0.9, size=(80,))
    initial_values_U = np.random.uniform(0.4, 0.9, size=(10,))

    # Definir el cambio máximo permitido (0.1)
    max_change = 0.1

    # Generar datos de entrada aleatorios (0 o 1) para cada síntoma
    X = np.random.randint(2, size=(num_cases, num_symptoms))

    # Definir clases de enfermedades (0, 1, 2, 3)
    # Se asigna una clase para cada combinación única de síntomas
    # Esto es solo un ejemplo, puedes ajustar las reglas según sea necesario
    T = np.zeros((num_cases,2), dtype=int)

    # Asignar clases basadas en ciertos síntomas
    for i in range(num_cases):
        if X[i, 0] == 1 and X[i, 2] == 1:
            T[i][0] = 0
            T[i][1] = 1
        elif X[i, 4] == 1 and X[i, 6] == 1:
            T[i][0] = 1
            T[i][1] = 0
        elif X[i, 1] == 1 and X[i, 3] == 1 and X[i, 5] == 1:
            T[i][0] = 1
            T[i][1] = 1
    # Pesos de la red neuronal
    W = np.clip(initial_values_W + np.random.uniform(-max_change, max_change, size=(80,)), 0.4, 0.9)

    # Umbrales
    U = np.clip(initial_values_U + np.random.uniform(-max_change, max_change, size=(10,)), 0.4, 0.9)

    return X,T,W,U

def train():
    X,T,W,U = loadData()
    model = Sequential()
    model.add(Dense(8,input_dim=8,activation='relu'))
    model.add(Dense(8,activation='relu'))
    model.add(Dense(2,activation='sigmoid'))
    model.compile(loss='binary_crossentropy',optimizer='adam',metrics='accuracy')
    model.fit(X,T,epochs=1000)
    model_json = model.to_json()
    with open("apipy/model.json","w") as json_file:
        json_file.write(model_json)
    
    model.save_weights("apipy/model.h5")

def predict(model,X):
    respuesta0 = model.predict([X])
    respuesta1 = [round(i) for i in respuesta0[0]]
    return enfermedad(respuesta1[0],respuesta1[1])
  
def recibe(request,x0,x1,x2,x3,x4,x5,x6,x7,A):
  A = int(A)
  if A==0:
    X = [float(x0),float(x1),float(x2),float(x3),float(x4),float(x5),float(x6),float(x7)]
    json_file = open("apipy/model.json",'r')
    loaded_model_json = json_file.read()
    json_file.close()
    loaded_model = model_from_json(loaded_model_json)
    loaded_model.load_weights("apipy/model.h5")
    loaded_model.compile(loss='binary_crossentropy',optimizer='adam',metrics='accuracy')
    resultado = predict(loaded_model,X)
    print(resultado)
  else:
    train()
    resultado = "OK"
  return HttpResponse(resultado)