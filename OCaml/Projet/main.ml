(* ------------------------------------------------ *)
(* ------------------------------------------------ *)
(* -------------------- Divers  ------------------- *)
(* ------------------------------------------------ *)
(* ------------------------------------------------ *)

type joueur = {mutable prenom : string;
               mutable credits : int;
               mutable nbVictoires : int};;

type couleur = Coeur | Carreau | Trefle | Pique;;

type carte = Carte of int * couleur;;

let valide (Carte(i, _)) = 1 <= i && i <= 14;;

let affiche_carte (Carte(i, c)) =
  print_string (match i with
    | 11 -> "Valet"
    | 12 -> "Dame"
    | 13 -> "Roi"
    | 14 -> "As" (* As fort *)
    | 1 -> "As" (* As faible *)
    | _ -> string_of_int i);
  print_string " de ";
  print_string (match c with
    | Coeur -> "Coeur"
    | Carreau -> "Carreau"
    | Trefle -> "Trefle"
    | Pique -> "Pique");;

let rec affiche_liste_cartes = function
  | [] -> ()
  | a::l -> affiche_carte a;
            print_newline ();
	    affiche_liste_cartes l;;

let genere_jeu () =
  let rec aux = function
    | 0 -> []
    | n -> Carte(n, Coeur)::Carte(n, Carreau)::
           Carte(n, Pique)::Carte(n, Trefle)::(aux (n-1)) in
  aux 13;;

let genere_mini_jeu i j =
  let rec aux n = if n > j then [] else Carte(n, Coeur)::Carte(n, Pique)::(aux (n+1)) in
  aux i;;

let rec melanger = function
  | [] -> []
  | lst -> let rec aux l0 n = (match l0, n with
             | [], _ -> failwith "Impossible"
             | a::l, 0 -> a, l
	     | a::l, n -> let u, v = aux l (n-1) in (u, a::v)) in
	   let u, v = aux lst (Random.int (List.length lst)) in
	   u::(melanger v);;

let distribue lst =
  let rec aux l1 l2 = function
    | [] -> l1, l2
    | a::l -> aux (a::l2) l1 l in
  aux [] [] lst;;

let rec piocher n lst = match n, lst with
  | 0, _ -> [], lst
  | n, [] -> failwith "Pas assez de cartes"
  | n, a::l -> let u, v = piocher (n-1) l in (a::u, v);;

let rec empiler_cartes lst = function
  | [] -> lst
  | a::l -> a::(empiler_cartes lst l);;

(* ------------------------------------------------ *)
(* ------------------------------------------------ *)
(* ------------------- Blackjack  ----------------- *)
(* ------------------------------------------------ *)
(* ------------------------------------------------ *)

type plateau_blackjack = (carte list) * (carte list) * (carte list);;

let creer_blackjack () = ([], [], melanger(genere_jeu ()));;

let banque_pioche n (j, b, p) =
  let u, v = piocher n p in (j, empiler_cartes u b, v);;

let joueur_pioche n (j, b, p) =
  let u, v = piocher n p in (empiler_cartes u j, b, v);;

let valeur_carte (Carte(n, c)) = if n < 10 then n else 10;;

let rec total_cartes = function
  | [] -> 0
  | a::l -> (valeur_carte a) + (total_cartes l);;

let rec faire_jouer_banque (j, b, p) =
  if total_cartes b < total_cartes j
    then faire_jouer_banque (banque_pioche 1 (j, b, p))
    else (j, b, p);;

let afficher_jeu (j, b, p) =
  print_endline "***** Banque ******";
  affiche_liste_cartes b;
  print_string "Total = ";
  print_int (total_cartes b);
  print_newline ();
  print_endline "***** Joueur ******";
  affiche_liste_cartes j;
  print_string "Total = ";
  print_int (total_cartes j);
  print_newline ();
  print_endline "*******************";;

let rec faire_jouer_joueur (j, b, p) =
  afficher_jeu (j, b, p);
  let rec aux () =
    print_string "Voulez vous piocher (oui/non) ? ";
    match read_line () with
      | "oui" -> faire_jouer_joueur (joueur_pioche 1 (j, b, p))
      | "non" -> (j, b, p);
      | _ -> print_endline "Choix invalide"; aux () in
  if total_cartes j <= 21 then aux () else (j, b, p);;

let jouer_blackjack data_joueur =
  print_endline "********************************************";
  print_endline "*                 Blackjack                *";
  print_endline "********************************************";
  let p0 = melanger (genere_jeu ()) in
  let j1, b1, p1 = joueur_pioche 2 ([], [], p0) in
  let j2, b2, p2 = banque_pioche 1 (j1, b1, p1) in
  let j3, b3, p3 = faire_jouer_joueur (j2, b2, p2) in
  let total_joueur = total_cartes j3 in
  if total_joueur > 21
  then print_endline "Vous avez perdu"
  else begin
    let j4, b4, p4 = faire_jouer_banque (j3, b3, p3) in
    afficher_jeu (j4, b4, p4);
    let total_banque = total_cartes b4 in
    if total_banque > 21 || total_joueur > total_banque
    then begin
      data_joueur.credits <- data_joueur.credits + 1;
      print_endline "Vous avez gagne"
    end
    else if total_joueur < total_banque
    then print_endline "Vous avez perdu"
    else print_endline "Egalite..."
  end;
  print_string "Vous avez desormais ";
  print_int data_joueur.credits;
  print_endline " credit(s).";;
  
(* ------------------------------------------------ *)
(* ------------------------------------------------ *)
(* ------------------- Bataille  ------------------ *)
(* ------------------------------------------------ *)
(* ------------------------------------------------ *)

let affiche_diagramme bat j1 j2 =
  List.iter (fun _ -> print_string "|") j1;
  print_string " ";
  List.iter (fun _ -> print_string "?") bat;
  print_string " ";
  List.iter (fun _ -> print_string "|") j2;;

let affiche_fight a b =
  print_string "   ";
  affiche_carte a;
  print_string "   VS   ";
  affiche_carte b;
  let x = match a with Carte(i, _) -> i in
  let y = match b with Carte(i, _) -> i in
  if x = y then print_string "  BATAILLE !";;

let rec choix_carte () =
  print_string "Jeu de carte de X à As, X = ? ";  
  match String.lowercase (read_line ()) with
    | "roi" | "13" -> 13
    | "dame" | "12" -> 12
    | "valet" | "11" -> 11
    | "dix" | "10" -> 10
    | "neuf" | "9" -> 9
    | "huit" | "8" -> 8
    | "sept" | "7" -> 7
    | "six" | "6" -> 6
    | "cinq" | "5" -> 5
    | "quatre" | "4" -> 4
    | "trois" | "3" -> 3
    | "deux" | "2" -> 2
    | _ -> print_endline "Choix invalide. "; choix_carte ();;

let jouer_bataille data_joueur =
  print_endline "********************************************";
  print_endline "*                 Bataille                 *";
  print_endline "********************************************";
  let rec aux bat j1 j2 =
    print_string data_joueur.prenom;
    print_string " ";
    affiche_diagramme bat j1 j2;
    print_string " Adversaire ";
    match j1, j2 with
      | _, [] -> print_newline (); true
      | [], _ -> print_newline (); false
      | a::l1, b::l2 ->
	affiche_fight a b;
	print_newline ();
	let x = match a with Carte(i, _) -> i in
	let y = match b with Carte(i, _) -> i in
      	if x < y
	then aux [] l1 (empiler_cartes (melanger (a::b::bat)) l2)
	else if x > y
	then aux [] (empiler_cartes (melanger (a::b::bat)) l1) l2
	else (match l1, l2 with
	  | _, [] -> true
	  | [], _ -> false
	  | ax::l1x, bx::l2x -> aux (ax::bx::a::b::bat) l1x l2x) in
  let nb = choix_carte () in
  let jeu = melanger (genere_mini_jeu nb 14) in
  let j1, j2 = distribue jeu in
  if aux [] j1 j2
  then (print_endline "Vous avez gagne !";
	data_joueur.nbVictoires <- data_joueur.nbVictoires + 1)
  else (print_endline "Vous avez perdu...";
	data_joueur.credits <- data_joueur.credits - 1);
  print_string "Vous avez desormais ";
  print_int data_joueur.credits;
  print_endline " credit(s).";;

(* ------------------------------------------------ *)
(* ------------------------------------------------ *)
(* ---------------- Menu pricipal  ---------------- *)
(* ------------------------------------------------ *)
(* ------------------------------------------------ *)

let rec menu_principal j =
  print_endline "********************************************";
  Printf.printf "*  Credits %03d       Batailles gagnees %03d *\n" j.credits j.nbVictoires;
  print_endline "*  Menu principal :                        *";
  print_endline "*  1. Jouer au blackjack                   *";
  print_endline "*  2. Jouer à la bataille                  *";
  print_endline "*  3. Quitter                              *";
  print_endline "********************************************";
  let rec aux () =
    print_string "Que voulez vous faire ? ";
    match String.lowercase (read_line ()) with
    | "1" | "blackjack" ->
      jouer_blackjack j;
      menu_principal j
    | "2" | "bataille"  ->
      if j.credits > 0
      then (jouer_bataille j; menu_principal j)
      else (print_endline "Vous n'avez plus de jetons"; aux ())
    | "3" | "quitter" ->
      print_string "Au revoir ";
      print_endline j.prenom
    | "4" | "tricher" ->
      print_string "Randomseed ? ";
      Random.init (read_int ());
      aux ();
    | "5" | "gagner" ->
      print_endline "Felicitation, vous avez gagne un jeton !";
      j.credits <- j.credits + 1;
      aux ();
    | _ ->
      print_endline "Choix invalide";
      aux () in
  aux ();;

let _ = 
  Random.self_init ();
  print_string "Quel est ton prenom ? ";
  let p = read_line () in
  menu_principal {prenom = p; credits = 0; nbVictoires = 0};;

(* ------------------------------------------------ *)
(* ------------------------------------------------ *)
(* -------------------- Tests  -------------------- *)
(* ------------------------------------------------ *)
(* ------------------------------------------------ *)

(*
affiche_liste_cartes (genere_jeu ());;
print_newline ();;
affiche_liste_cartes (genere_mini_jeu 0 15);;
print_newline ();;
affiche_liste_cartes (melanger (genere_jeu ()));;
print_newline ();;
*)
let melange_rate () =
  let rec aux = function 0 -> true
    | n -> let a = Random.int n in
           let b = Random.int (n-1) in
           if a mod 2 = 0 then if a = b then aux (n-2) else false
	   else if a = b+1 then aux (n-2) else false in
  for i=0 to 10000000 do
    Random.init i;
    if aux 18 then (print_int i; print_newline ())
  done;;

Random.init 5936020;;
affiche_liste_cartes (melanger (genere_mini_jeu 6 14));;

