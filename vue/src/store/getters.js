
export default {

	count(state, getters){
		return Math.pow(state.count, getters.weight);
	},
}
