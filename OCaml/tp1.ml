(***** Exercice 1 *****)
let rec fact = function
  | 0 -> 1
  | n when n < 0 -> failwith "Nombre négatif"
  | n -> n * fact (n-1);;

fact 5;;

(***** Exercice 2 *****)
let rec bin n = if n > 0
  then (n mod 2) + (10 * bin (n/2))
  else 0;;

print_int (bin 17);;
print_newline ();;

(***** Exercice 3 *****)
let rec fibo = function
  | 0 -> 0
  | 1 -> 1
  | n -> (fibo (n-1)) + (fibo (n-2));;

(* exactement fibo n appels *)

let fibo2 n =
  let rec fibo_aux = function
    | 0 -> (0, 1)
    | u -> let a, b = fibo_aux (u-1) in (b, a+b) in
  fst (fibo_aux n);;

for i=0 to 10 do
print_int (fibo i);
print_string "\t";
print_int (fibo2 i);
print_newline ();
done;;

(***** Exercice 4 *****)
let max a b = if a > b then a else b;;

let truc f g x = f x (g x);;

(***** Exercice 5 *****)
let hd = function
  | a::l -> a
  | [] -> failwith "Out of bound";;

let rec length = function
  | a::l -> 1 + length l
  | [] -> 0;;

let rec nth l n = match l, n with
  | [], _ -> failwith "Out of bound"
  | a::l, 1 -> a
  | a::l, n -> nth l (n-1);;

let rev l =
  let rec rev pile = function
    | [] -> pile
    | a::l -> rev (a::pile) l in
  rev [] l;;

let rec append a b = match a with
  | [] -> b
  | a::l -> a::(append l b);;

let rec map f = function
  | [] -> []
  | a::l -> (f a)::(map f l);;

(***** Exercice 6 *****)
let rec fold f x = function
  | [] -> x
  | a::l -> fold f (f x a) l;;

let rec forall p = function
  | [] -> true
  | a::l -> (p a) && (forall p l);;

let forall p l = fold (fun s x -> s && p x) true l;;

let rec exists p = function
  | [] -> false
  | a::l -> (p a) || (forall p l);;

let exists p l = fold (fun s x -> s || p x) false l;;

(***** Exercice 7 *****)
let rec tri_fusion l = 
  let rec split a b = function
    | [] -> a, b
    | u::v -> split b (u::a) v in
  let rec fusionne a b = match a, b with
    | [], _  -> b
    | _ , [] -> a
    | x::u, y::v -> if x < y
                      then x::(fusionne u b)
                      else y::(fusionne a v) in
  if length l <= 1
    then l
    else let l1, l2 = split [] [] l in
      fusionne (tri_fusion l1) (tri_fusion l2);;

(***** Exercice 8 *****)
type arbre = Vide | Noeud of int * arbre * arbre ;;

let rec taille = function
  | Vide -> 0
  | Noeud(_, a, b) -> 1 + (taille a) + (taille b);;  

let rec nb_feuilles = function
  | Vide -> 0
  | Noeud(_, Vide, Vide) -> 1
  | Noeud(_, a, b) -> (nb_feuilles a) + (nb_feuilles b);;

let rec hauteur = function
  | Vide -> 0
  | Noeud(_, a, b) -> 1 + max (hauteur a) (hauteur b);;

let rec detect arb n = match arb with
  | Vide -> false
  | Noeud(x, a, b) -> x == n || (detect a n) || (detect b n);;
  
let complet arb =
  let rec aux = function
    | Vide -> (0, 0)
    | Noeud(_, a, b) -> let min1, max1 = aux a in
                        let min2, max2 = aux b in
               (1 + min min1 min2, 1 + max max1 max2) in
  let mini, maxi = aux arb in mini = maxi;;
(* Un arbre est complet si toutes ses feuilles
   sont a la meme hauteur, ie si min = max *)

