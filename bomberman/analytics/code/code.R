setwd('d:/workspace/projects/szelenin/bomberman/analytics/')
data<-read.csv2('data/choppers-analyze.txt', sep = ',')

set.seed(123)
#Split dataset into training and testing sets

smp_size <- floor(0.75 * nrow(data)) #75% will be training
train_ind <- sample(seq_len(nrow(data)), size = smp_size)
train <-data[train_ind,]
test <- data[-train_ind,]

cor(as.numeric(data$Next), as.numeric(data$Previous))
library(corrgram)

#install.packages('caret')
#install.packages("e1071")
library(caret)



numData<-data.frame(Next=as.numeric(data$Next), 
                    Previous=as.numeric(data$Previous),
                    Up=as.numeric(data$Up),
                    Down=as.numeric(data$Down),
                    Left=as.numeric(data$Left),
                    Right=as.numeric(data$Right))
head(numData)
corrgram(numData, upper.panel=panel.ellipse)

summary(aov(data = numData, formula = Next ~ Previous * Up * Down * Left * Right))

model <- lm(Next ~ Previous + Up + Down + Left + Right, numData)

#try tree
library(rpart)

treeModel <- rpart(Next ~ Previous + Up + Right + Down + Left, method="class", data=train)
plot(treeModel)
text(treeModel)

predictions <- predict(treeModel, newdata=test, type="class")
print(confusionMatrix(predictions, test$Next))

library(randomForest)
