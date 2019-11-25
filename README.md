![ELIXIR_GREECE_and_ESPA_logos](http://genomics-lab.fleming.gr/fleming/reczko/elixir/logos/ELIXIR_GREECE_and_ESPA_logos-1338x218.png)


# GClass
G-Class: A Divide and Conquer Application for Grid Protein Classification

Protein classification has always been one of the major challenges in bioinformatics. Prediction of the functional behavior of proteins is enabled by the presence of motifs in protein chains. The correlation between protein properties and their motifs is not obvious, since multiple motifs may coexist in a protein chain and combined they determine its function. Due to the complexity of this correlation, most data mining algorithms are either non-efficient or time-consuming. One solution to this problem lies in the parallelization of the classification procedure.

GClass is an application implementing a methodology for parallel classification that utilizes grid technologies. First, data are split into multiple sets while preserving the original data distribution. Each one of these data sets is then used to train a separate classification model. Finally, all models are combined to produce the final classification rules, which are used to classify new proteins or test the methodology itself. Experiments have shown a considerable increase in execution speed with no loss in classification accuracy. In fact, there are several experiments where an improvement in accuracy was observed. Moreover, classification rules were extracted from experiments that were previously too memory-demanding to be conducted in one computer. Other internal characteristics, such as classification algorithm independency and data adaptability, along with a user-friendly graphical interface can facilitate usage and adoption of the procedure.
