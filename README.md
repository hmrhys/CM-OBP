# CoffeeMaker


*Line Coverage (should be >=70%)*

![Coverage](.github/badges/jacoco.svg)

*Branch Coverage (should be >=50%)*

![Branches](.github/badges/branches.svg)

## REST API Endoints

### Name of the function:createRecipe(Recipe r)
<br>Route:POST api/v1/recipes/</br>
<br>Description:This API endpoint create a new recipe in the system</br>
<br>Input: The input would be a Recipe object with a price value and the amounts for types of ingredients needed for the recipe.</br>
<br>Output:Below. 200/Ok status </br>
<br>{ </br>
<br>'id' : '1',</br>
<br>'name': 'Coffee',</br>
<br>'price': '5',</br>
<br>ingredients: [{id:1, name:'suger', amount: 1}, {id:2, name: 'milk', amount: 2}, {id:3, name:'chocolate', amount: 2}, {id:4, name: 'coffee', amount: 2}] </br>
<br>}</br>


### Name of the function:updateInventory(Inventory v)
<br>Route:PUT api/v1/inventory/</br>
<br>Description:This API endpoint update the inventory in the system</br>
<br>Input: The input would be a Inventory object and update the ingredients in the inventory.</br>
<br>Output:Below. 200/Ok status </br>
<br>{ </br>
<br>'id' : '1',</br>
<br>ingredients: [{id:1, name:'suger', amount: 100}, {id:2, name: 'milk', amount: 200}, {id:3, name:'chocolate', amount: 200}, {id:4, name: 'coffee', amount: 200}</br>
<br>}</br>


### Name of the function:createIngredient(Ingredient i)
<br>Route:POST api/v1/ingredients/</br>
<br>Description:This API endpoint creates a new ingrdient object and add it to the database</br>
<br>Input: The input would be an ingredient object with the values for the ingredident.</br>
<br>Output:Below. 200/Ok status </br>
<br>{ </br>
<br>'id' : '5',</br>
<br>'name': 'Soymilk',</br>
<br>'amount': '5',</br>
<br>}</br>


### Name of the function:getIngredient(Ingredient i)
<br>Route:GET api/v1/ingredients/</br>
<br>Description:This API endpoint retrieve an ingredient from database</br>
<br>Output:Below. 200/Ok status </br>
[{"id":2,"ingredient":"COFFEE","amount":100},{"id":3,"ingredient":"MILK","amount":100},{"id":4,"ingredient":"SUGAR","amount":100},{"id":5,"ingredient":"CHOCOLATE","amount":100},{"id":6,"ingredient":"VANILLA","amount":24},{"id":7,"ingredient":"VANILLA","amount":124},{"id":8,"ingredient":"CREAM","amount":2},{"id":9,"ingredient":"CREAM","amount":102},{"id":11,"ingredient":"SUGAR","amount":2},{"id":12,"ingredient":"VANILLA","amount":2}]

### Name of the function:getRecipe(Recipe r)
<br>Route:GET api/v1/recipes/{name}</br>
<br>Description:This API endpoint retrrieve a recipe by the name of the recipe stored in the system</br>
<br>Output:Below. 200/Ok status </br>
<br>{ </br>
<br>'id' : '1',</br>
<br>'name': 'Coffee',</br>
<br>'price': '5',</br>
<br>ingredients: {"id":5,"ingredient":"CHOCOLATE","amount":100} </br>
<br>}</br>
