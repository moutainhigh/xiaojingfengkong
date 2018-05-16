export function getItemsId(array) {
  let ids = [];
  for (let index in array){
    ids.push(array[index].id)
  }
  console.log(ids);
  return ids;
}
