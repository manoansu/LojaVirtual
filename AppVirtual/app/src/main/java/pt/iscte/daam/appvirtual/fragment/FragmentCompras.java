package pt.iscte.daam.appvirtual.fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.cards.actions.BaseSupplementalAction;
import it.gmariotti.cardslib.library.cards.actions.IconSupplementalAction;
import it.gmariotti.cardslib.library.cards.material.MaterialLargeImageCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;
import it.gmariotti.cardslib.library.view.CardViewNative;
import pt.iscte.daam.appvirtual.R;
import pt.iscte.daam.appvirtual.util.Constantes;

/**
 * Created by amane on 06/05/2017.
 */
public class FragmentCompras extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_compras, container, false);

        final RelativeLayout lytLoading = (RelativeLayout) viewRoot.findViewById(R.id.lytLoading);
        lytLoading.setVisibility(View.GONE);

        Card card = new Card(getContext());

        CardHeader header = new CompraInnerHeader(getContext());

        header.setPopupMenu(R.menu.menu_main, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                Toast.makeText(getActivity(), "Click on " + item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        card.addCardHeader(header);

        CardViewNative cardView = (CardViewNative) viewRoot.findViewById(R.id.carddemo);
        cardView.setCard(card);

        Card cardCollapse = new Card(getContext());

        header = new CardHeader(getContext());
        header.setOtherButtonVisible(true);
        header.setOtherButtonDrawable(R.drawable.ic_notifications_none_24dp);
        header.setOtherButtonClickListener(new CardHeader.OnClickCardHeaderOtherButtonListener() {
            @Override
            public void onButtonItemClick(Card card, View view) {
                Toast.makeText(getActivity(), "Click on Other Button", Toast.LENGTH_LONG).show();
            }
        });
        header.setTitle("Mochila Mickey");
        cardCollapse.addCardHeader(header);

        CardViewNative cardViewCollapse = (CardViewNative) viewRoot.findViewById(R.id.cardCollapse);
        cardViewCollapse.setCard(cardCollapse);


        //TextView txtTitulo = (TextView) cardViewMaterial.findViewById(R.id.card_main_inner_simple_title);
        //txtTitulo.setTextColor(Color.GREEN);

        CardArrayRecyclerViewAdapter mCardArrayAdapter = new CardArrayRecyclerViewAdapter(getActivity(), carregarMaterialRecycler(viewRoot));

        //Staggered grid view
        CardRecyclerView mRecyclerView = (CardRecyclerView) viewRoot.findViewById(R.id.carddemo_recyclerview);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Set the empty view
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mCardArrayAdapter);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.ic_person_24dp)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");

        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        return viewRoot;
    }

    private ArrayList<Card> carregarMaterialRecycler(View viewRoot) {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < 3; i++) {
            // Set supplemental actions as icon
            ArrayList<BaseSupplementalAction> actions = new ArrayList<BaseSupplementalAction>();

            IconSupplementalAction t1 = new IconSupplementalAction(getActivity(), R.id.ic1);
            t1.setOnActionClickListener(new BaseSupplementalAction.OnActionClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Toast.makeText(getActivity(), " Click on Text SHARE ", Toast.LENGTH_SHORT).show();
                }
            });
            actions.add(t1);

            IconSupplementalAction t2 = new IconSupplementalAction(getActivity(), R.id.ic2);
            t2.setOnActionClickListener(new BaseSupplementalAction.OnActionClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Toast.makeText(getActivity(), " Click on Text LEARN ", Toast.LENGTH_SHORT).show();
                }
            });
            actions.add(t2);

            MaterialLargeImageCard cardMaterial =
                    MaterialLargeImageCard.with(getActivity())
                            .setTextOverImage("Italian Beaches " + i)
                            .setTitle("Titulo Exemplo " + i)
                            .setSubTitle("Subtitulo Exemplo " + i)
                            .useDrawableExternal(new MaterialLargeImageCard.DrawableExternal() {
                                @Override
                                public void setupInnerViewElements(ViewGroup parent, View viewImage) {
                                    Picasso.with(getActivity())
                                            .load("http://www.best-beaches.com/images/europe/italy-beach.jpg")
                                            .error(R.drawable.header)
                                            .into((ImageView) viewImage);
                                }
                            })
                            .setupSupplementalActions(R.layout.carddemo_native_material_supplemental_actions_large_icon, actions)
                            .build();

            CardViewNative cardViewMaterial = (CardViewNative) viewRoot.findViewById(R.id.carddemo_largeimage);
            cardViewMaterial.setCard(cardMaterial);

            cards.add(cardMaterial);
        }

        return cards;
    }

    private class CompraInnerHeader extends CardHeader {

        public CompraInnerHeader(Context context) {
            super(context, R.layout.linha_header_compra);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            TextView txtTitulo = (TextView) view.findViewById(R.id.txtTitulo);
            TextView txtDescricao = (TextView) view.findViewById(R.id.txtDescricao);
            ImageView imgProduto = (ImageView) view.findViewById(R.id.imgProduto);

            txtTitulo.setText("Profissão Teste");
            txtDescricao.setText("Descrição Teste");
            Picasso.with(imgProduto.getContext()).load(Constantes.URL_WEB_BASE + "img/produtos/colecao_lapis_cor.jpg").into(imgProduto);
        }
    }
}
