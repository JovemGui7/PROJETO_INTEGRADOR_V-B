# PROJETO_INTEGRADOR_V-B

SISTEMA DE SENSORIAL COM ARDUINO UNO

Alunos: Guilherme de Souza Brito

Professor: Thalles Bruno























1.	INTRODUÇÃO

O crescente avanço das tecnologias de Internet e das Coisas IoT, tem viabilizado soluções inovadoras para o monitoramento e automação de ambientes residenciais. Nesse contexto, este projeto foi desenvolvido com o objetivo de transformar uma residência convencional em um ambiente inteligente, capaz de coletar, processar e exibir dados ambientais em tempo real.

O projeto integra um hardware embarcado, representado por uma placa Arduino UNO com sensores de temperatura, umidade e luminosidade, a um módulo de software desenvolvido em linguagem Java, responsável pelo processamento e análise dos dados coletados.A interface de visualização foi projetada para dispositivos móveis (smartphones), permitindo o usuário acompanhar as condições do ambiente de forma prática e intuitiva.

A proposta do projeto não apenas responde essa carencia no setor, mas também aplica, de forma integrada, os conteúdos estudados na disciplina, como IoT, Big Data, programação orientada a objetos, diagrama e prototipação de interfaces.















2.	DESENVOLVIMENTO
   
2.1 ESCOPO DO PROJETO

O sistema foi idealizado para resolver um problema prático e recorrente: a dificuldade de monitorar, de forma centralizada e em tempo real, as condições ambientais de diferentes cômodos de uma residência. Sem um sistema integrado, o morador não tem visibilidade sobre variações de temperatura que possam comprometer o conforto ou a saúde, níveis de umidade que favoreçam o surgimento de mofo, ou condições de luminosidade inadequadas para as atividades do dia a dia.

O escopo foi definido de forma objetiva para este protótipo, contemplando os seguintes requisitos funcionais:

•	Coletar dados de temperatura , umidade relativa e luminosidade por meio de sensores físicos conectados ao Arduino UNO.

•	Processar os dados coletados, avaliando cada grandeza com base em limites predefinidos e gerando um status.

•	Publicar as leituras processadas de forma estruturada, incluindo serialização em formato JSON para integração futura com APIs e sistemas web.

•	Apresentar as informações em uma interface visual mobile, com gráficos em tempo real e alertas automáticos baseados nas condições detectadas.

Como requisito não funcional, o sistema foi projetado para ser modular, de fácil manutenção e expansível, permitindo a adição de novos sensores ou funcionalidades sem a necessidade de reestruturar a arquitetura existente.

2.2 O QUE FOI DESENVOLVIDO

•	Protótipo de Hardware – Arduino UNO Tinkercad

O protótipo de hardware foi desenvolvido no ambiente Tinkercad Circuits, simulando uma placa Arduino UNO conectada a dois sensores:

•	Sensor DHT11: responsável pela leitura simultânea de temperatura e umidade relativa, conectado ao pino digital D2 da placa. O sensor utiliza protocolo digital de comunicação de um fio, sendo controlado pela biblioteca DHT.h.

•	Sensor LDR: responsável pela leitura de luminosidade, conectado ao pino analógico A0. Para garantir uma leitura estável de tensão, foi utilizado um divisor de tensão com resistor de 10 kΩ conectado ao GND.

O código embarcado, escrito em linguagem C/C++ para Arduino, realiza a leitura periódica dos sensores a cada dois segundos, avalia o status de cada grandeza utilizando as funções evaluateTemperature(), evaluateHumidity() e evaluateLuminosity(), e transmite os dados pela porta serial USB no seguinte formato padronizado:
TEMP:XX.X,HUMID:XX.X,LUM:XXXX,TS:XXXXXXXX,TEMP_STATUS:X,HUMID_STATUS:X,LUM_STATUS:X

Esse formato foi projetado para ser consumido diretamente pelo módulo Java, garantindo interoperabilidade entre as duas camadas do sistema.

•	Módulo de Software – Java

O módulo Java foi desenvolvido com arquitetura orientada a objetos, seguindo rigorosamente o diagrama de classes definido para o projeto. O sistema é composto por seis classes com responsabilidades bem delimitadas.

Main: Ponto de entrada e orquestração do fluxo

ArduinoSimulator: Simula a leitura dos sensores físicos 

SensorData: Armazena dados brutos dos sensores 

DataProcessor: Processa e avalia as leituras dos dados

ProcessedData: Armazena dados processados com status

ConsolePublisher: Publica os dados processados no console

O fluxo de execução do sistema segue o seguinte ciclo, orquestrado pela classe Main:

•	A classe ArduinoSimulator executa o método readSensors(), que gera um objeto SensorData contendo os valores brutos de temperatura, umidade, luminosidade e o timestamp da leitura.

•	O objeto SensorData é passado para DataProcessor.process(), que invoca internamente os três métodos de avaliação e constrói um objeto ProcessedData com os campos de status preenchidos.

•	O objeto ProcessedData é então passado para ConsolePublisher.publish(), que formata e exibe a leitura no console, incluindo alertas quando algum valor estiver fora dos limites. O método toJSON() serializa os dados para um formato estruturado pronto para integração com APIs.

Em um ambiente de produção real, a classe ArduinoSimulator seria substituída por uma implementação que lê dados diretamente da porta serial USB utilizando a biblioteca jSerialComm, e a classe ConsolePublisher poderia ser estendida para publicar via protocolo MQTT ou REST API.

2.3 PROTOTIPO DA INTERFACE VISUAL MOBILE

A interface visual foi projetada seguindo os princípios de usabilidade e a experiência do usuário voltados para dispositivos móveis. O protótipo interativo apresenta algumas telas, do login a tela principal de resultados:

•	Tela SensorData: exibe os dados brutos capturados pelo ArduinoSimulator, com barras de progresso indicando o valor relativo de cada sensor e badges coloridos indicando o status atual.

•	Tela ProcessedData: apresenta o retorno do método toJSON() formatado, os três campos de status e um gráfico de linhas com o histórico das últimas leituras, distinguindo temperatura, umidade e luminosidade por cor.

2.4 DIFICULDADES ENCONTRADAS

Ao longo do desenvolvimento do projeto, algumas dificuldades técnicas e conceituais foram identificadas:

•	Integração hardware-software: o principal desafio conceitual foi garantir que a nomenclatura utilizada no código Arduino (funções evaluateTemperature(), evaluateHumidity() e evaluateLuminosity()) fosse espelhada fielmente nas classes Java (DataProcessor), mantendo consistência semântica entre as duas camadas da solução.

•	Formato de comunicação: a definição do formato CSV transmitido pelo Arduino precisou ser atenciosamente planejado para incluir todos os campos necessários ao módulo Java, temperatura, umidade, luminosidade, timestamp e status.































3.	CONCLUSÃO



O desenvolvimento do sistema sensorial vai além da entrega de um protótipo funcional. Na prática, mostrou como tecnologias emergentes, quando bem integradas, conseguem transformar ambientes físicos em espaços inteligentes e adaptados às necessidades do dia a dia.

Durante o projeto, foi possível vivenciar o ciclo completo de desenvolvimento de um sistema IoT: desde a concepção da ideia e a montagem do protótipo no Tinkercad Circuits, passando pela modelagem com o diagrama de classes, até a implementação do módulo Java e a criação da interface mobile. Cada etapa ajudou a aprofundar conhecimentos importantes do curso de Análise e Desenvolvimento de Sistemas, conectando até mesmo as disciplinas como programação orientada a objetos.

Do ponto de vista técnico, a arquitetura se mostrou sólida. A divisão de responsabilidades entre as classes ArduinoSimulator, DataProcessor, ConsolePublisher, SensorData e ProcessedData garantiu um código limpo e preparado. O protocolo de comunicação entre o firmware Arduino e o módulo Java também reflete o escopo do  projeto, permitindo que as duas camadas evoluam de forma independente.

No hardware, o protótipo simulado no Tinkercad validou a viabilidade das conexões entre o Arduino UNO, o sensor DHT11 e o fotoresistor LDR. A coerência entre as funções de avaliação do firmware e os métodos da classe DataProcessor reforça que hardware e software foram pensados juntos, compartilhando os mesmos critérios de decisão.
A interface mobile, com suas telas SensorData, ProcessedData, mostrou como um protótipo de média fidelidade consegue comunicar o valor do sistema ao usuário final de forma clara e direta.

É claro que o projeto tem limitações próprias do contexto acadêmico: a comunicação com o Arduino é simulada, e a interface ainda não é um aplicativo publicado. Mas reconhecer essas limitações é justamente o que permite planejar e progredir nos próximos passo. Como a integração com comunicação real, protocolos IoT como MQTT, banco de dados para histórico de leituras e notificações para alertas remotos.
No fim, o projeto cumpriu o que se propôs.


4. ANEXOS

 Link do prototipo mobile:   file:///C:/Users/GUILHERME%20BRITO/OneDrive/Desktop/COD_PROJ_INTEGRADOR/smart_sensorial_prototipo.html


5. BIBLIOGRAFIA

https://www.lucidchart.com/ acesso em 6 de abril de 2026.

https://www.tinkercad.com/ acesso em 6 de abril de 2026.




